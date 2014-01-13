package com.simple.engine.service.hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;

public class OperationTool implements Tool {

	private Configuration configuration;
	
	@Override
	public int run(String args[]) throws IOException, InterruptedException, ClassNotFoundException {
		JobConf jobConf = new JobConf(getConf());
		
		FileInputFormat.addInputPath(jobConf, new Path("/tmp/stocks.txt"));
		FileOutputFormat.setOutputPath(jobConf, new Path("output1"));
		Job job = new Job(jobConf);
		
		//job.setInputFormatClass(StockInputFormat.class);
		
		job.setJarByClass(Executor.class);
		job.setMapperClass(OperationMapper.class);
 
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
}
