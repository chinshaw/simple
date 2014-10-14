package com.simple.orchestrator.server.hadoop.mrv2;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.JobID;
import org.apache.hadoop.mapreduce.Cluster;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobStatus;
import org.apache.hadoop.yarn.event.AsyncDispatcher;

import com.google.common.eventbus.EventBus;
import com.simple.api.exceptions.RAnalyticsException;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.orchestrator.api.IJobProgress;
import com.simple.orchestrator.api.conf.IHadoopJobConfiguration;
import com.simple.orchestrator.api.exception.HadoopJobException;
import com.simple.orchestrator.api.exception.InvalidJobIdException;
import com.simple.orchestrator.api.hadoop.operation.IHadoopOperation;
import com.simple.orchestrator.api.metric.MetricKey;
import com.simple.orchestrator.api.service.IOperationExecutionService;
import com.simple.orchestrator.server.ModuleProperties;
import com.simple.orchestrator.server.hadoop.config.ConfigurationException;
import com.simple.orchestrator.server.hadoop.config.HttpInputConf;
import com.simple.orchestrator.server.hadoop.config.OperationConfig;
import com.simple.orchestrator.server.hadoop.io.MetricWritable;
import com.simple.orchestrator.server.hadoop.io.format.MetricInputFormat;
import com.simple.orchestrator.server.hadoop.io.format.MetricInputFormat.InputAdapterType;
import com.simple.orchestrator.server.hadoop.io.format.MetricOutputFormat;
import com.simple.orchestrator.server.hadoop.io.format.MetricOutputFormat.OutputAdapterType;
import com.simple.orchestrator.server.hadoop.job.ArtisanConfiguration;
import com.simple.orchestrator.server.hadoop.job.ArtisanJob;

public class OperationDriver implements IOperationExecutionService {

	private static final Logger logger = Logger.getLogger(OperationDriver.class
			.getName());

	private static ModuleProperties props = ModuleProperties.getInstance();

	public static final String TIMESTAMP_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

	/**
	 * Event bus for eventing.
	 */
	private EventBus eventBus;

	private AsyncDispatcher dispatcher;

	/**
	 * Default constructor with the event bus.
	 */
	@Inject
	public OperationDriver(EventBus eventBus) {
		this.eventBus = eventBus;
		dispatcher = createDispatcher();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * This will fire off the job and return the job id in string format, this
	 * can be used to register status listeners for the job status.
	 * 
	 * @param jobOwner
	 * @param operationInputs
	 * @param operation
	 * @param dataProviders
	 * @return
	 */
	public String execute(IHadoopJobConfiguration jobDetails)
			throws HadoopJobException {
		try {

			ArtisanJob job = createJob(jobDetails);
			dispatcher.init(job.getConfiguration());
			dispatcher.start();
			job.submit();
			return job.getJobID().toString();
		} catch (ClassNotFoundException | IOException | InterruptedException
				| RAnalyticsException | ConfigurationException e) {
			logger.log(Level.SEVERE, "Unable to execute job", e);
			throw new HadoopJobException("Unable to execute operation", e);
		} catch (Exception e) {
			throw new HadoopJobException("Unable to execute operation", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * This will connect to the service and kill the running job found by it's
	 * jobId.
	 */
	@Override
	public void stop(String jobId) throws InvalidJobIdException,
			HadoopJobException {
		try {
			getJobById(JobID.forName(jobId)).killJob();
		} catch (IllegalArgumentException e) {
			throw new InvalidJobIdException("Invalid job id " + jobId);
		} catch (IOException | InterruptedException e) {
			throw new HadoopJobException("Unable to stop job " + jobId, e);
		}

	}

	/**
	 * Get the progress of the job, this will return the {@link IJobProgress}
	 * which will have more information about the status of the job.
	 * 
	 */
	@Override
	public IJobProgress progress(final String jobId)
			throws InvalidJobIdException, HadoopJobException {
		try {
			final Job job = getJobById(JobID.forName(jobId));
			return new IJobProgress() {

				@Override
				public float getPercentageComplete() {
					try {
						JobStatus status = job.getStatus();
						float totalProgress = status.getMapProgress()
								+ status.getReduceProgress()
								+ status.getCleanupProgress();
						return (totalProgress / 3);
					} catch (IOException | InterruptedException e) {
						return -1;
					}
				}

				@Override
				public String getErrors() {
					try {
						return job.getStatus().getFailureInfo();
					} catch (IOException | InterruptedException e) {
						return "Unable to communicate with cluster "
								+ e.getMessage();
					}
				}
			};

		} catch (IllegalArgumentException e) {
			throw new InvalidJobIdException("Invalid job id" + jobId);
		} catch (IOException | InterruptedException e) {
			throw new HadoopJobException("Unable to retrive job from cluster",
					e);
		}
	}

	/**
	 * Helper to create a job based on an IHadoopOperationJobConfiguration
	 * 
	 * @param details
	 * @return
	 * @throws IOException
	 * @throws ConfigurationException
	 * @throws RAnalyticsException
	 */
	ArtisanJob createJob(IHadoopJobConfiguration details)
			throws IOException, ConfigurationException, RAnalyticsException {

		IHadoopOperation mapperOperation =  details.getMapperOperation();
		IHadoopOperation reducerOperation = details.getReducerOperation();

		OperationConfig conf = new OperationConfig();

		ArtisanJob job = new ArtisanJob(new ArtisanConfiguration());
		conf.setDataProviders((List) details.getDataProviders());
		conf.setOperationInputs((List) details.getUserInputs());

		job.setMapperClass(ROperationMapper.class);
		job.setReducerClass(ROperationReducer.class);
		job.setInputFormatClass(MetricInputFormat.class);
		job.setOutputFormatClass(MetricOutputFormat.class);
		job.setMapOutputKeyClass(MetricKey.class);
		job.setMapOutputValueClass(MetricWritable.class);

		// Configuration configuration = job.getConfiguration();

		job.getConfiguration()
				.set("conf.column",
						props.getProperty("com.artisan.orchestrator.hbase.metric.colfamily"));

		MetricOutputFormat.setOutputAdatperType(job.getConfiguration(),
				OutputAdapterType.HBASE);
		MetricInputFormat.setInputAdapterType(job.getConfiguration(),
				InputAdapterType.HTTP);

		job.getConfiguration()
				.set(HttpInputConf.WEB_URL_PROPERTY,
						"http://ichart.finance.yahoo.com/table.csv?s=HPQ&a=00&b=12&c=2013&d=00&e=15&f=2014&g=d&ignore=.csv");

		job.setMapperOperation(mapperOperation);
		job.setReducerOperation(reducerOperation);

		return job;
	}

	/**
	 * Helper to get the job by it's id
	 * 
	 * @param jobID
	 * @return the job
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private Job getJobById(JobID jobID) throws IOException,
			InterruptedException {
		return getCluster().getJob(jobID);
	}

	/**
	 * Helper to get the cluster
	 * 
	 * @return
	 * @throws IOException
	 */
	private Cluster getCluster() throws IOException {
		Cluster cluster = new Cluster(new Configuration());
		return cluster;
	}

	protected AsyncDispatcher createDispatcher() {
		return new AsyncDispatcher();
	}
}