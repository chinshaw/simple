package com.simple.engine.service.hadoop;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ToolRunner;

import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.dataprovider.HttpDataProvider;
import com.simple.domain.model.metric.Metric;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.engine.service.AnalyticsOperationException;
import com.simple.engine.service.IAnalyticsOperationExecutor;
import com.simple.engine.service.hadoop.io.HttpInputFormat;
import com.simple.engine.service.hadoop.io.NullInputFormat;
import com.simple.original.api.exceptions.RAnalyticsException;

public class OperationExecutor implements IAnalyticsOperationExecutor {

	private static final Logger logger = Logger
			.getLogger(OperationExecutor.class.getName());

	public OperationExecutor() {

	}

	@Override
	public HashMap<Long, Metric> execute(String jobOwner,
			List<AnalyticsOperationInput> userInputs,
			AnalyticsOperation operation, List<DataProvider> dataProviders)
			throws AnalyticsOperationException {

		try {
			return _execute(userInputs, operation, dataProviders);
		} catch (RAnalyticsException e) {
			throw new AnalyticsOperationException(
					"Unable to execute operation " + operation.getName(), e);
		}
	}

	@Override
	public void execute(String jobOwner, AnalyticsOperation operation,
			List<DataProvider> dataProviders)
			throws AnalyticsOperationException {
	}

	private synchronized HashMap<Long, Metric> _execute(
			List<AnalyticsOperationInput> operationInputs,
			AnalyticsOperation operation, List<DataProvider> dataProviders)
			throws RAnalyticsException {

		if (!(operation instanceof RAnalyticsOperation)) {
			throw new RAnalyticsException("Invalid operation type");
		}

		RAnalyticsOperation rOperation = (RAnalyticsOperation) operation;

		logger.finest("Operation code is " + rOperation.getCode());
		OperationConfig configuration = new OperationConfig();

		try {
			configuration.setOperation(rOperation);
			configuration.setDataProviders(dataProviders);
			configuration.setOperationInputs(operationInputs);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		try {
			// configuration.set("fs.defaultFS", "hdfs://127.0.0.1:9000");
			configuration.set("mapreduce.framework.name", "yarn");
			configuration.set("yarn.resourcemanager.address", "localhost:8032");
			// configuration.set("yarn.nodemanager.aux-services",
			// "mapreduce.shuffle");

			// Job job = createJob(configuration);
			// job.setJar("/Users/chris/devel/workspace/simple/engine/target/simple-analytics-engine-1.1-SNAPSHOT.jar");
			// job.setJarByClass(ROperationMapper.class);
			// job.waitForCompletion(true);

			// ToolRunner tr = new ToolRunner();

			ToolRunner.run(configuration, new OperationTool(), new String[] {});
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unable to execute job", e);
		}

		return null;
	}

	public Job createJob(Configuration configuration) throws JAXBException,
			IOException {
		Job job = Job.getInstance(configuration);

		OperationConfig opConfig = new OperationConfig(configuration);

		List<DataProvider> dataproviders = opConfig.getDataProviders();

		if (dataproviders != null) {
			logger.fine("Found data providers count " + dataproviders.size());
			DataProvider dp = dataproviders.get(0);
			if (dp instanceof HttpDataProvider) {
				HttpInputFormat.setInput(job, ((HttpDataProvider) dp).getUrl());
			}
		} else {
			logger.fine("Setting NullInputFormat for input");
			NullInputFormat.setInput(job);
		}

		AnalyticsOperation operation = opConfig.getOperation();
		String jobName = operation.getName();

		String outputPath = "/tmp/" + "hd_" + jobName + "-" + UUID.randomUUID();
		logger.fine("Job output path is " + outputPath);
		FileOutputFormat.setOutputPath(job, new Path(outputPath));

		job.setJobName(jobName);
		job.setJarByClass(OperationExecutor.class);
		job.setMapperClass(ROperationMapper.class);

		return job;
	}

	@Override
	public boolean isOperationSuccessful() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reset() throws RAnalyticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	}
}
