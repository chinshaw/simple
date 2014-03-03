package com.simple.engine.service.hadoop.mrv1;

import java.io.IOException;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class MetricOutputFormat<K extends IMetricKey, V extends IMetricWritable>
		extends OutputFormat<K, V> implements Configurable {

	private OutputFormatAdapter<K, V> adapter;
	
	private Configuration conf_;
	
	public MetricOutputFormat() {
	}
	
	@Override
	public RecordWriter<K, V> getRecordWriter(TaskAttemptContext context)
			throws IOException, InterruptedException {
		return adapter.getRecordWriter(context);
	}

	@Override
	public void checkOutputSpecs(JobContext context) throws IOException,
			InterruptedException {
		adapter.checkOutputSpecs(context);
	}

	@Override
	public OutputCommitter getOutputCommitter(TaskAttemptContext context)
			throws IOException, InterruptedException {
		return adapter.getOutputCommitter(context);
	}

	@Override
	public void setConf(Configuration conf) {
		this.conf_ = conf;
		this.adapter = MetricAdapterFactory.createAdapter(conf);
	}

	@Override
	public Configuration getConf() {
		return conf_;
	}
}
