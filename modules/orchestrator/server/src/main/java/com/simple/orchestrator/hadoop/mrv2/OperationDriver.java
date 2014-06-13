package com.simple.orchestrator.hadoop.mrv2;

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

import com.google.common.eventbus.EventBus;
import com.simple.api.exceptions.RAnalyticsException;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.orchestrator.api.IHadoopOperationJobConfiguration;
import com.simple.orchestrator.api.IJobProgress;
import com.simple.orchestrator.api.exception.HadoopJobException;
import com.simple.orchestrator.api.exception.InvalidJobIdException;
import com.simple.orchestrator.api.metric.MetricKey;
import com.simple.orchestrator.api.service.IOperationExecutionService;
import com.simple.orchestrator.hadoop.ModuleProperties;
import com.simple.orchestrator.hadoop.config.ConfigurationException;
import com.simple.orchestrator.hadoop.config.HttpInputConf;
import com.simple.orchestrator.hadoop.io.MetricWritable;
import com.simple.orchestrator.hadoop.io.format.MetricInputFormat;
import com.simple.orchestrator.hadoop.io.format.MetricInputFormat.InputAdapterType;
import com.simple.orchestrator.hadoop.io.format.MetricOutputFormat;
import com.simple.orchestrator.hadoop.io.format.MetricOutputFormat.OutputAdapterType;
import com.simple.orchestrator.hadoop.job.AnalyticsOperationHadoopJob;

public class OperationDriver implements IOperationExecutionService {

	private static final Logger logger = Logger.getLogger(OperationDriver.class.getName());

	private static ModuleProperties props = ModuleProperties.getInstance();

	public static final String TIMESTAMP_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

	private EventBus eventBus;
	
	/**
	 * 
	 */
	@Inject
	public OperationDriver(EventBus eventBus) {
		this.eventBus = eventBus;
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
	public String execute(IHadoopOperationJobConfiguration jobDetails) throws HadoopJobException {
		try {
			AnalyticsOperationHadoopJob job = createJob(jobDetails);
			job.submit();
			return job.getJobID().toString();
		} catch (ClassNotFoundException | IOException | InterruptedException | RAnalyticsException
				| ConfigurationException e) {
			logger.log(Level.SEVERE, "Unable to execute job", e);
			throw new HadoopJobException("Unable to execute operation", e);
		} catch (Exception e) {
			throw new HadoopJobException("Unable to execute operation", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 *  This will connect to the service and kill the running job found by it's jobId.
	 */
	@Override
	public void stop(String jobId) throws InvalidJobIdException, HadoopJobException {
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
	public IJobProgress progress(final String jobId) throws InvalidJobIdException,
			HadoopJobException {
		try {
			final Job job = getJobById(JobID.forName(jobId));
			return new IJobProgress() {

				@Override
				public float getPercentageComplete() {
					try {
						JobStatus status = job.getStatus();
						float totalProgress = status.getMapProgress() + status.getReduceProgress()
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
						return "Unable to communicate with cluster " + e.getMessage();
					}
				}
			};

		} catch (IllegalArgumentException e) {
			throw new InvalidJobIdException("Invalid job id" + jobId);
		} catch (IOException | InterruptedException e) {
			throw new HadoopJobException("Unable to retrive job from cluster", e);
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
	static AnalyticsOperationHadoopJob createJob(IHadoopOperationJobConfiguration details)
			throws IOException, ConfigurationException, RAnalyticsException {

		if (details.getOperation() == null) {
			throw new RAnalyticsException("operation == null");
		}

		RAnalyticsOperation rop = (RAnalyticsOperation) details.getOperation();

		AnalyticsOperationHadoopJob job = AnalyticsOperationHadoopJob.getInstance();
		job.setOperation(rop);

		job.setDataProviders((List) details.getDataProviders());
		job.setOperationInputs((List) details.getUserInputs());

		job.setJobName(details.getOperation().getName());

		job.setMapperClass(ROperationMapper.class);
		job.setReducerClass(ROperationReducer.class);
		job.setInputFormatClass(MetricInputFormat.class);
		job.setOutputFormatClass(MetricOutputFormat.class);
		job.setMapOutputKeyClass(MetricKey.class);
		job.setMapOutputValueClass(MetricWritable.class);

		Configuration configuration = job.getConfiguration();

		configuration.set("conf.column",
				props.getProperty("com.artisan.orchestrator.hbase.metric.colfamily"));

		MetricOutputFormat.setOutputAdatperType(configuration, OutputAdapterType.HBASE);
		MetricInputFormat.setInputAdapterType(configuration, InputAdapterType.HTTP);

		configuration
				.set(HttpInputConf.WEB_URL_PROPERTY,
						"http://ichart.finance.yahoo.com/table.csv?s=HPQ&a=00&b=12&c=2013&d=00&e=15&f=2014&g=d&ignore=.csv");

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
	private Job getJobById(JobID jobID) throws IOException, InterruptedException {
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
}