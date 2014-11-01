package com.artisan.orchestrator.hadoop.hadoop.mrv2;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Logger;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.server.jobtracker.JTConfig;
import org.apache.hadoop.util.Tool;

import com.artisan.orchestrator.hadoop.job.config.OperationConfiguration;
import com.artisan.orchestrator.hadoop.job.mapper.ROperationMapper;
import com.simple.orchestrator.api.conf.impl.ConfigurationException;
import com.simple.orchestrator.api.hadoop.operation.Operation;

@Deprecated
public class OperationTool implements Tool {

	private static final Logger logger = Logger.getLogger(OperationTool.class.getName());

	private Configuration configuration;

	@Override
	public int run(String args[]) throws IOException, InterruptedException, ClassNotFoundException, ConfigurationException {

		Configuration configuration = getConf();
		Job job = Job.getInstance(configuration);
		// job.setJar("/Users/chris/devel/workspace/simple/engine/target/simple-analytics-engine-1.1-SNAPSHOT-jar-with-dependencies.jar");
		job.setUser("chris");

		Operation operation = OperationConfiguration.getOperation(getConf());
		String jobName = operation.getJobName();

		String outputPath = "/tmp/" + "hd_" + jobName + "-" + UUID.randomUUID();
		logger.fine("Job output path is " + outputPath);
		FileOutputFormat.setOutputPath(job, new Path(outputPath));

		job.setJobName(jobName);
		job.setJarByClass(OperationDriver.class);
		job.setMapperClass(ROperationMapper.class);

		// listJobConfProperties();
		getJobTrackerHostPort();

		return job.waitForCompletion(true) ? 0 : 1;
	}

	@Override
	public void setConf(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public Configuration getConf() {
		return configuration;
	}

	/**
	 * Prints out the jobconf properties on stdout when verbose is specified.
	 */
	protected void listJobConfProperties() {
		msg("==== JobConf properties:");
		TreeMap<String, String> sorted = new TreeMap<String, String>();
		for (final Map.Entry<String, String> en : getConf()) {
			sorted.put(en.getKey(), en.getValue());
		}
		for (final Map.Entry<String, String> en : sorted.entrySet()) {
			msg(en.getKey() + "=" + en.getValue());
		}
		msg("====");
	}

	protected String getJobTrackerHostPort() {
		return getConf().get(JTConfig.JT_IPC_ADDRESS);
	}

	protected void msg(String msg) {
		System.out.println("STREAM: " + msg);
	}

}
