package com.simple.engine.service.hadoop;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class HttpInputFormat extends InputFormat<LongWritable, Text> implements Configurable {

	public static class HttpInputSplit extends InputSplit implements Writable {

		@Override
		public long getLength() throws IOException, InterruptedException {
			return 100;
		}

		@Override
		public String[] getLocations() throws IOException, InterruptedException {
			return new String[] {};
		}

		@Override
		public void write(DataOutput out) throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void readFields(DataInput in) throws IOException {
			// TODO Auto-generated method stub
			
		}
	}
	
	private Configuration configuration;
	
	@Override
	public void setConf(Configuration configuration) {
		this.configuration = configuration;	
	}

	@Override
	public Configuration getConf() {
		return configuration;
	}
	
	@Override
	public List<InputSplit> getSplits(JobContext context) throws IOException, InterruptedException {
			List<InputSplit> splits = new ArrayList<InputSplit>();
			splits.add(new HttpInputSplit());
			return splits;
	}

	@Override
	public RecordReader<LongWritable, Text> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
		String requestUrl = configuration.get(WebInputConf.WEB_URL_PROPERTY);
		return new HttpRecordReader(requestUrl);
	}
	
	public static void setInput(Job job, String url) {
	    job.getConfiguration().set(WebInputConf.WEB_URL_PROPERTY, url);
		job.setInputFormatClass(HttpInputFormat.class);	
	}
}
