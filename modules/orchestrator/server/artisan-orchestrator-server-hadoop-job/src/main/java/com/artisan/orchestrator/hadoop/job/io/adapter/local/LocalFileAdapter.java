package com.artisan.orchestrator.hadoop.job.io.adapter.local;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import com.artisan.orchestrator.hadoop.job.io.adapter.AbstractOutputFormatAdapter;
import com.artisan.orchestrator.server.api.IMetricWritable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.api.orchestrator.IMetricKey;
import com.simple.orchestrator.api.conf.impl.LocalFileAdapterConfiguration;

public class LocalFileAdapter<K extends IMetricKey, V extends IMetricWritable>
extends AbstractOutputFormatAdapter<K, V> implements Configurable {
	
	public class LocalFileRecordWriter extends RecordWriter<K, V> {

		private ObjectMapper mapper = new ObjectMapper();
		
		@Override
		public void write(K key, V value) throws IOException, InterruptedException {
			String filePath = getConf().get(LocalFileAdapterConfiguration.OUTPUT_FILE_PATH);
			LocalMetricContainer metricContainer = new LocalMetricContainer(key, value);
			mapper.writeValue(new File(filePath), metricContainer);
		}

		@Override
		public void close(TaskAttemptContext context) throws IOException, InterruptedException {
		}
	}
	
	@Override
	public RecordWriter<K, V> getRecordWriter(TaskAttemptContext context) throws IOException, InterruptedException {
		return new LocalFileRecordWriter();
	}

	@Override
	public void checkOutputSpecs(JobContext context) throws IOException, InterruptedException {
	}

	@Override
	public OutputCommitter getOutputCommitter(TaskAttemptContext context) throws IOException, InterruptedException {
		return new LocalFileOutputCommitter();
	}
	
	public static void setPath(Configuration configuration, String filePath) {
		if (filePath == null) {
			throw new IllegalArgumentException("null file path");
		}
		configuration.set(LocalFileAdapterConfiguration.OUTPUT_FILE_PATH, filePath);
	}
}