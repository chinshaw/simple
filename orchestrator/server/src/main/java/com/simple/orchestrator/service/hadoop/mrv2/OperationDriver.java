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
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapreduce.Job;

import com.simple.api.exceptions.RAnalyticsException;
import com.simple.api.orchestrator.IAnalyticsOperation;
import com.simple.api.orchestrator.IMetric;
import com.simple.domain.model.AnalyticsOperationOutput;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.orchestrator.api.IJobProgress;
import com.simple.orchestrator.api.IOperationExecutionResponse;
import com.simple.orchestrator.api.IOperationExecutionService;
import com.simple.orchestrator.api.IOperationJob;
import com.simple.orchestrator.api.exception.InvalidJobIdException;
import com.simple.orchestrator.api.exception.JobException;
import com.simple.orchestrator.metric.MetricKey;
import com.simple.orchestrator.service.AnalyticsOperationException;
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

	private static final Logger logger = Logger.getLogger(OperationDriver.class.getName());

	private static ModuleProperties props;
	
	private Boolean jobSuccess;

	public static final String TIMESTAMP_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";


	public OperationDriver() {
		try {
			props = ModuleProperties.getInstance();
		} catch (IOException e) {
			throw new RuntimeException("Unable to instantiate module properties", e);
		}
	}

	public HashMap<Long, IMetric> execute(String jobOwner, List<AnalyticsOperationInput> userInputs, IAnalyticsOperation operation,
			List<DataProvider> dataProviders) throws AnalyticsOperationException, ConfigurationException {

		try {
			return _execute(jobOwner, userInputs, operation, dataProviders);
		} catch (RAnalyticsException e) {
			throw new AnalyticsOperationException("Unable to execute operation " + operation.getName(), e);
		}
	}

	public void execute(String jobOwner, IAnalyticsOperation operation, List<DataProvider> dataProviders)
			throws AnalyticsOperationException, ConfigurationException {
		try {
			_execute(jobOwner, null, operation, dataProviders);
		} catch (RAnalyticsException e) {
			throw new AnalyticsOperationException("Unable to execute operation " + operation.getName(), e);
		}
	}

	private HashMap<Long, IMetric> _execute(final String jobOwner, final List<AnalyticsOperationInput> operationInputs,
			final IAnalyticsOperation operation, List<DataProvider> dataProviders) throws RAnalyticsException, ConfigurationException {

		if (operation == null) {
			throw new RAnalyticsException("operation == null");
		}

		RAnalyticsOperation rop = (RAnalyticsOperation) operation;

		try {
			
			// Set configurations before creating job, otherwise they are lost
			// not sure why though???
			AnalyticsOperationHadoopJob job = AnalyticsOperationHadoopJob.getInstance();
			job.setOperation(rop);
			job.setDataProviders(dataProviders);
			job.setOperationInputs(operationInputs);
			
			job.setJobName(operation.getName());

			job.setMapperClass(ROperationMapper.class);
			job.setReducerClass(ROperationReducer.class);
			job.setInputFormatClass(MetricInputFormat.class);
			job.setOutputFormatClass(MetricOutputFormat.class);
			job.setMapOutputKeyClass(MetricKey.class);
			job.setMapOutputValueClass(MetricWritable.class);
			
			Configuration configuration = job.getConfiguration();
			
			configuration.set(TableOutputFormat.OUTPUT_TABLE, props.getProperty("com.artisan.orchestrator.hbase.metric.table"));
			configuration.set("conf.column", props.getProperty("com.artisan.orchestrator.hbase.metric.colfamily"));
			configuration.set("fs.defaultFS", "file:///");
			configuration.set("mapred.job.tracker", "local");
			
			
			MetricOutputFormat.setOutputAdatperType(configuration, OutputAdapterType.HBASE );
			MetricInputFormat.setInputAdapterType(configuration, InputAdapterType.HTTP);
			
			configuration.set(HttpInputConf.WEB_URL_PROPERTY, "http://ichart.finance.yahoo.com/table.csv?s=HPQ&a=00&b=12&c=2013&d=00&e=15&f=2014&g=d&ignore=.csv");

			jobSuccess = job.waitForCompletion(true);
			if (jobSuccess != true) {
				throw new RAnalyticsException("Job failed, see logs");
			}

			List<AnalyticsOperationOutput> outputs = (List<AnalyticsOperationOutput>) operation.getOutputs();
			return grabMetrics(outputs, job.getConfiguration());
			
		} catch (ClassNotFoundException | IOException | InterruptedException e) {
			logger.log(Level.SEVERE, "Unable to execute job", e);
			throw new RAnalyticsException("Unable to execute operation", e);
		}
	}
	
	/**
	 * Used to create a job that will run on the cluster.
	 * 
	 * @param configuration
	 * @return
	 * @throws JAXBException
	 * @throws IOException
	 */
	protected Job createJob(OperationConfig configuration) throws JAXBException, IOException {
		Job job = Job.getInstance(configuration);

		configuration.set("fs.defaultFS", "hdfs://127.0.0.1:9000");
		configuration.set("mapreduce.framework.name", "yarn");
		configuration.set("yarn.resourcemanager.address", "localhost:8032");
		configuration.set("yarn.nodemanager.aux-services", "mapreduce.shuffle");

		job.setJar("/Users/chris/devel/workspace/simple/engine/target/simple-analytics-engine-1.1-SNAPSHOT.jar");

		return job;
	}

	
	protected HashMap<Long, IMetric> grabMetrics(final List<AnalyticsOperationOutput> opOutputs, Configuration conf) throws IOException, InterruptedException {
		HTable table = new HTable(conf, "metrics");
		HashMap<Long, IMetric> outputs = new HashMap<Long, IMetric>();
		for (AnalyticsOperationOutput output: opOutputs) {
			Get get = new Get(Bytes.toBytes(output.getName()));
			get.addFamily(Bytes.toBytes("rexp"));
			get.setMaxVersions(1);
			Result result = table.get(get);
			
			byte[] bytes = result.getValue(Bytes.toBytes("rexp"), null);
			//Metric metric = Metric.fromBytes(bytes);
			//outputs.put(output.getId(), metric);
		}
		table.close();
		
		return outputs;
	}


	public void attemptStop(String jobId) {
		//JobClient client = new JobClient(conf)
		
	}

	@Override
	public IOperationExecutionResponse executeSynchronous(IOperationJob operationJob) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String execute(IOperationJob operationJob) throws JobException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stop(String jobId) throws InvalidJobIdException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IJobProgress progress(String jobId) throws InvalidJobIdException {
		// TODO Auto-generated method stub
		return null;
	}
}
