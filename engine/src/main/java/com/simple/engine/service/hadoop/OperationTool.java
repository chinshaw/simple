package com.simple.engine.service.hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

public class OperationTool implements Tool {

	private Configuration configuration;
	
	@Override
	public int run(String args[]) throws IOException, InterruptedException, ClassNotFoundException {
		Job job = Job.getInstance(getConf());
		
		
		//FileInputFormat.setInputPaths(job, "/tmp/stocks.txt");
		HttpInputFormat.setInput(job, "http://chart.yahoo.com/table.csv?s=aapl");
		FileOutputFormat.setOutputPath(job, new Path("output1"));
		
		//job.setInputFormatClass(StockInputFormat.class);
		
		job.setJarByClass(Executor.class);
		job.setMapperClass(ROperationMapper.class);
 
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
