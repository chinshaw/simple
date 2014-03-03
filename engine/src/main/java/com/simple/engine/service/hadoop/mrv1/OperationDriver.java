package com.simple.engine.service.hadoop.mrv1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.mapreduce.MultiTableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.io.compress.CompressionInputStream;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import com.hadoop.compression.lzo.LzoCodec;
import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.dataprovider.HttpDataProvider;
import com.simple.domain.model.metric.Metric;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.engine.service.AnalyticsOperationException;
import com.simple.engine.service.IAnalyticsOperationExecutor;
import com.simple.engine.service.hadoop.Utils;
import com.simple.engine.service.hadoop.io.HttpInputFormat;
import com.simple.engine.service.hadoop.io.NullInputFormat;
import com.simple.original.api.exceptions.RAnalyticsException;
import com.simple.radapter.protobuf.REXPProtos.REXP;
import com.twitter.elephantbird.mapreduce.output.LzoProtobufB64LineOutputFormat;
import com.twitter.elephantbird.util.HadoopCompat;
import com.twitter.elephantbird.util.TypeRef;

public class OperationDriver implements IAnalyticsOperationExecutor {

	private static final Logger logger = Logger.getLogger(OperationDriver.class.getName());

	private Boolean jobSuccess;

	public static final String TIMESTAMP_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

	static TypeRef<REXP> REXP_TYPE = new TypeRef<REXP>(REXP.class) {
	};

	public OperationDriver() {

	}

	@Override
	public HashMap<Long, Metric> execute(String jobOwner, List<AnalyticsOperationInput> userInputs, AnalyticsOperation operation,
			List<DataProvider> dataProviders) throws AnalyticsOperationException, ConfigurationException {

		try {
			return _execute(jobOwner, userInputs, operation, dataProviders);
		} catch (RAnalyticsException e) {
			throw new AnalyticsOperationException("Unable to execute operation " + operation.getName(), e);
		}
	}

	@Override
	public void execute(String jobOwner, AnalyticsOperation operation, List<DataProvider> dataProviders)
			throws AnalyticsOperationException, ConfigurationException {
		try {
			_execute(jobOwner, null, operation, dataProviders);
		} catch (RAnalyticsException e) {
			throw new AnalyticsOperationException("Unable to execute operation " + operation.getName(), e);
		}
	}

	private HashMap<Long, Metric> _execute(final String jobOwner, final List<AnalyticsOperationInput> operationInputs,
			final AnalyticsOperation operation, List<DataProvider> dataProviders) throws RAnalyticsException, ConfigurationException {

		if (operation == null) {
			throw new RAnalyticsException("operation == null");
		}

		RAnalyticsOperation rop = (RAnalyticsOperation) operation;

		try {
			
			// Set configurations before creating job, otherwise they are lost
			// not sure why though???
			OperationConfig configuration = new OperationConfig();
			configuration.setOperation(rop);
			configuration.setDataProviders(dataProviders);
			configuration.setOperationInputs(operationInputs);

			
			Job job = createLocalJob(configuration);
			job.setJobName(operation.getName());

			job.setMapperClass(ROperationMapper.class);
			job.setReducerClass(ROperationReducer.class);

			job.setOutputFormatClass(TableOutputFormat.class);
			job.getConfiguration().set(TableOutputFormat.OUTPUT_TABLE, "metrics");
			job.getConfiguration().set("conf.column", "rexp");
			
			
			// Job data providers
			List<DataProvider> dataproviders = configuration.getDataProviders();

			if (dataproviders != null) {
				DataProvider dp = dataproviders.get(0);
				if (dp instanceof HttpDataProvider) {
					HttpInputFormat.setInput(job, ((HttpDataProvider) dp).getUrl());
				}
			} else {
				NullInputFormat.setInput(job);
			}

			jobSuccess = job.waitForCompletion(true);
			if (jobSuccess != true) {
				throw new RAnalyticsException("Job failed, see logs");
			}

			return grabMetrics(configuration);
			// return grabMetrics(outputPath);
		} catch (ClassNotFoundException | IOException | InterruptedException e) {
			logger.log(Level.SEVERE, "Unable to execute job", e);
			throw new RAnalyticsException("Unable to execute operation", e);
		}
	}
	
	
	private void configureHbase(Configuration configuration) {
		Configuration hbaseConf = HBaseConfiguration.create(configuration);
		
		
	}

	/**
	 * This is used to create a local job that will run in the current jvm.
	 * 
	 * @param configuration
	 * @return
	 * @throws IOException
	 * @throws JAXBException
	 * @throws IllegalStateException
	 */
	protected Job createLocalJob(OperationConfig configuration) throws IOException {
		Job job = Job.getInstance(configuration);
		configuration.set("fs.defaultFS", "file:///");
		configuration.set("mapred.job.tracker", "local");

		return job;
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

	
	protected HashMap<Long, Metric> grabMetrics( final Configuration conf) throws IOException, InterruptedException {
		HashMap<Long, Metric> outputs = new HashMap<Long, Metric>();
		
		
		return outputs;
	}

	@Override
	public boolean isOperationSuccessful() {
		return jobSuccess;
	}

	@Override
	public void reset() throws RAnalyticsException {
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	}



}
