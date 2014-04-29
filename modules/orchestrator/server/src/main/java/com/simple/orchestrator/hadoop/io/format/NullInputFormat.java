package com.simple.orchestrator.hadoop.io.format;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import com.simple.api.orchestrator.IMetricKey;
import com.simple.orchestrator.api.IMetricWritable;

/**
 * Simple class that does not provide any input, the mapper
 * receives an empty list of splits.
 * 
 * @author chris
 */
public class NullInputFormat extends InputFormat<IMetricKey, IMetricWritable> implements Configurable {

	@Override
	public void setConf(Configuration conf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Configuration getConf() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RecordReader<IMetricKey, IMetricWritable> createRecordReader(InputSplit arg0,
			TaskAttemptContext arg1) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InputSplit> getSplits(JobContext arg0) throws IOException,
			InterruptedException {
		return new ArrayList<InputSplit>();
	}

	public static void setInput(Job job) {
		job.setInputFormatClass(NullInputFormat.class);	
	}

}
