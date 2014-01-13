package com.simple.engine.service.hadoop;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class HttpInputFormat extends InputFormat<LongWritable, Text> implements Configurable {

	class HttpInputSplit extends InputSplit {

		@Override
		public long getLength() throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String[] getLocations() throws IOException, InterruptedException {
			return new String[] {};
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
		
	}

	@Override
	public RecordReader<LongWritable, Text> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
		String requestUrl = configuration.get("request_url");
		return new HttpRecordReader(requestUrl);
	}

}
