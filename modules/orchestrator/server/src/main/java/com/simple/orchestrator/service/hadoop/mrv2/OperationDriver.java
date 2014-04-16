package com.simple.orchestrator.service.hadoop.mrv2;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapred.JobID;
import org.apache.hadoop.mapreduce.Cluster;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobStatus;

import com.simple.api.exceptions.RAnalyticsException;
import com.simple.api.orchestrator.IMetric;
import com.simple.domain.model.AnalyticsOperationOutput;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.orchestrator.api.IHadoopOperationJobConfiguration;
import com.simple.orchestrator.api.IJobProgress;
import com.simple.orchestrator.api.IOperationExecutionService;
import com.simple.orchestrator.api.exception.HadoopJobException;
import com.simple.orchestrator.api.exception.InvalidJobIdException;
import com.simple.orchestrator.metric.MetricKey;
import com.simple.orchestrator.service.hadoop.ModuleProperties;
import com.simple.orchestrator.service.hadoop.config.ConfigurationException;
import com.simple.orchestrator.service.hadoop.config.HttpInputConf;
import com.simple.orchestrator.service.hadoop.config.OperationConfig;
import com.simple.orchestrator.service.hadoop.io.MetricWritable;
import com.simple.orchestrator.service.hadoop.io.format.MetricInputFormat;
import com.simple.orchestrator.service.hadoop.io.format.MetricInputFormat.InputAdapterType;
import com.simple.orchestrator.service.hadoop.io.format.MetricOutputFormat;
import com.simple.orchestrator.service.hadoop.io.format.MetricOutputFormat.OutputAdapterType;
import com.simple.orchestrator.service.hadoop.job.AnalyticsOperationHadoopJob;

public class OperationDriver implements IOperationExecutionService {

	private static final Logger logger = Logger.getLogger(OperationDriver.class
			.getName());

	private static ModuleProperties props;

	private Boolean jobSuccess;

	public static final String TIMESTAMP_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

	public OperationDriver() {
		try {
			props = ModuleProperties.getInstance();
		} catch (IOException e) {
			throw new RuntimeException(
					"Unable to instantiate module properties", e);
		}
	}

	/**
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
			
		} catch (ClassNotFoundException | IOException | InterruptedException | RAnalyticsException | ConfigurationException e) {
			logger.log(Level.SEVERE, "Unable to execute job", e);
			throw new HadoopJobException("Unable to execute operation", e);
		} catch (Exception e) {
			throw new HadoopJobException("Unable to execute operation", e);
		}
	}

	/**
	 * Used to create a job that will run on the cluster.
	 * 
	 * @deprecated use create job below
	 * @param configuration
	 * @return
	 * @throws JAXBException
	 * @throws IOException
	 */
	private Job createJob(OperationConfig configuration)
			throws JAXBException, IOException {
		Job job = Job.getInstance(configuration);

		configuration.set("fs.defaultFS", "hdfs://127.0.0.1:9000");
		configuration.set("mapreduce.framework.name", "yarn");
		configuration.set("yarn.resourcemanager.address", "localhost:8032");
		configuration.set("yarn.nodemanager.aux-services", "mapreduce.shuffle");

		job.setJar("/Users/chris/devel/workspace/simple/engine/target/simple-analytics-engine-1.1-SNAPSHOT.jar");

		return job;
	}

	protected HashMap<Long, IMetric> grabMetrics(
			final List<AnalyticsOperationOutput> opOutputs, Configuration conf)
			throws IOException, InterruptedException {
		HTable table = new HTable(conf, "metrics");
		HashMap<Long, IMetric> outputs = new HashMap<Long, IMetric>();
		for (AnalyticsOperationOutput output : opOutputs) {
			Get get = new Get(Bytes.toBytes(output.getName()));
			get.addFamily(Bytes.toBytes("rexp"));
			get.setMaxVersions(1);
			Result result = table.get(get);

			byte[] bytes = result.getValue(Bytes.toBytes("rexp"), null);
			// Metric metric = Metric.fromBytes(bytes);
			// outputs.put(output.getId(), metric);
		}
		table.close();

		return outputs;
	}

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

	@Override
	public IJobProgress progress(final String jobId) throws InvalidJobIdException, HadoopJobException {
		try {
			final Job job = getJobById(JobID.forName(jobId));
			return new IJobProgress() {
		
				@Override
				public float getPercentageComplete() {
					try {
						JobStatus status = job.getStatus();
						float totalProgress = status.getMapProgress() + status.getReduceProgress() + status.getCleanupProgress();
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

	private AnalyticsOperationHadoopJob createJob(IHadoopOperationJobConfiguration details) throws IOException, ConfigurationException, RAnalyticsException {

		if (details.getOperation() == null) {
			throw new RAnalyticsException("operation == null");
		}

		RAnalyticsOperation rop = (RAnalyticsOperation) details.getOperation();
		
		// Set configurations before creating job, otherwise they are lost
		// not sure why though???
		AnalyticsOperationHadoopJob job = AnalyticsOperationHadoopJob
				.getInstance();
		job.setOperation(rop);
		
		job.setDataProviders((List)details.getDataProviders());
		job.setOperationInputs((List) details.getUserInputs());

		job.setJobName(details.getOperation().getName());

		job.setMapperClass(ROperationMapper.class);
		job.setReducerClass(ROperationReducer.class);
		job.setInputFormatClass(MetricInputFormat.class);
		job.setOutputFormatClass(MetricOutputFormat.class);
		job.setMapOutputKeyClass(MetricKey.class);
		job.setMapOutputValueClass(MetricWritable.class);
		
		Configuration configuration = job.getConfiguration();
		configuration.addResource(OperationDriver.class.getResourceAsStream("/configuration/hbase/hbase-client.xml"));
		
		configuration.set(TableOutputFormat.OUTPUT_TABLE, props
				.getProperty("com.artisan.orchestrator.hbase.metric.table"));
		configuration
				.set("conf.column",
						props.getProperty("com.artisan.orchestrator.hbase.metric.colfamily"));
		configuration.set("fs.defaultFS", "file:///");
		configuration.set("mapred.job.tracker", "local");

		MetricOutputFormat.setOutputAdatperType(configuration,
				OutputAdapterType.HBASE);
		MetricInputFormat.setInputAdapterType(configuration,
				InputAdapterType.HTTP);

		configuration
				.set(HttpInputConf.WEB_URL_PROPERTY,
						"http://ichart.finance.yahoo.com/table.csv?s=HPQ&a=00&b=12&c=2013&d=00&e=15&f=2014&g=d&ignore=.csv");

		return job;

	}
	
	private Job getJobById(JobID jobID) throws IOException, InterruptedException {
		return getCluster().getJob(jobID);
	}
	
	private Cluster getCluster() throws IOException {
		Cluster cluster = new Cluster(new Configuration());
		return cluster;
	}
}
