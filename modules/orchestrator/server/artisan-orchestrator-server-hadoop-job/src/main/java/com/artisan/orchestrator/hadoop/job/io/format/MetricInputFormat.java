package com.artisan.orchestrator.hadoop.job.io.format;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import com.artisan.orchestrator.hadoop.job.io.adapter.AbstractInputFormatAdapter;
import com.artisan.orchestrator.hadoop.job.io.adapter.MetricAdapterFactory;
import com.simple.api.orchestrator.IMetricKey;
import com.simple.orchestrator.api.IMetricWritable;

public class MetricInputFormat<K extends IMetricKey, V extends IMetricWritable>
		extends InputFormat<K, V> implements Configurable {

	
	public static final String INPUT_ADAPTER_TYPE = "com.simple.engine.service.hadoop.io.adapter.input.type";
	
	public enum InputAdapterType {
		NONE,
		HTTP
	}
	
	private AbstractInputFormatAdapter<K, V> adapter;
	
	private Configuration conf;
	
	@Override
	public void setConf(Configuration conf) {
		this.conf = conf;
		this.adapter = MetricAdapterFactory.createInputAdapter(conf);
	}

	@Override
	public Configuration getConf() {
		return conf;
	}

	@Override
	public List<InputSplit> getSplits(JobContext context) throws IOException,
			InterruptedException {
		return adapter.getSplits(context);
	}

	@Override
	public RecordReader<K, V> createRecordReader(InputSplit split,
			TaskAttemptContext context) throws IOException,
			InterruptedException {
		return adapter.createRecordReader(split, context);
	}
	
	public static final void setInputAdapterType(Configuration configuration, InputAdapterType type) {
		configuration.setEnum(INPUT_ADAPTER_TYPE, type);
	}
}