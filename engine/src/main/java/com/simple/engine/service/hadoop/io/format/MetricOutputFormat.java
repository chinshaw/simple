package com.simple.engine.service.hadoop.io.format;

import java.io.IOException;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import com.simple.engine.metric.IMetricKey;
import com.simple.engine.service.hadoop.io.IMetricWritable;
import com.simple.engine.service.hadoop.io.adapter.AbstractOutputFormatAdapter;
import com.simple.engine.service.hadoop.io.adapter.MetricAdapterFactory;

public class MetricOutputFormat<K extends IMetricKey, V extends IMetricWritable>
		extends OutputFormat<K, V> implements Configurable {

	public static final String OUTPUT_ADAPTER_TYPE = "com.simple.engine.service.hadoop.io.adapter.output.type";
	
	public enum OutputAdapterType {
		NONE,
		HBASE
	}
	
	private AbstractOutputFormatAdapter<K, V> adapter;
	
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
		this.adapter = MetricAdapterFactory.createOutputAdapter(conf);
	}

	@Override
	public Configuration getConf() {
		return conf_;
	}
	
	public static final void setOutputAdatperType(Configuration configuration, OutputAdapterType type) {
		configuration.setEnum(OUTPUT_ADAPTER_TYPE, type);
	}
}
