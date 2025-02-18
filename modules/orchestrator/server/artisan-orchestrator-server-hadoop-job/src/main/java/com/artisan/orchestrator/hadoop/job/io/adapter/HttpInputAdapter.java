package com.artisan.orchestrator.hadoop.job.io.adapter;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import com.artisan.orchestrator.hadoop.job.io.MetricWritable;
import com.artisan.orchestrator.hadoop.job.io.format.HttpInputFormat;
import com.artisan.orchestrator.server.api.IMetricWritable;
import com.simple.api.orchestrator.IMetricKey;
import com.simple.orchestrator.api.metric.MetricKey;
import com.simple.orchestrator.api.metric.MetricString;
import com.simple.orchestrator.api.rest.MediaType;

public class HttpInputAdapter<K extends IMetricKey, V extends IMetricWritable> extends
		AbstractInputFormatAdapter<K, V> {

	private static final Logger logger = Logger.getLogger(HttpInputAdapter.class.getName());
	
	class AdaptedRecordReader extends RecordReader<K, V> {

		private RecordReader<LongWritable, Text> adaptedReader;

		public AdaptedRecordReader(
				RecordReader<LongWritable, Text> adaptedReader) {
			this.adaptedReader = adaptedReader;
		}

		@Override
		public void initialize(InputSplit split, TaskAttemptContext context)
				throws IOException, InterruptedException {
			adaptedReader.initialize(split, context);
		}

		@Override
		public boolean nextKeyValue() throws IOException, InterruptedException {
			return adaptedReader.nextKeyValue();
		}

		@Override
		public K getCurrentKey() throws IOException,
				InterruptedException {
			LongWritable longWritable = adaptedReader.getCurrentKey();
			return (K) MetricKey.from(Bytes.toBytes(longWritable.get()));
		}

		@Override
		public V getCurrentValue() throws IOException,
				InterruptedException {
			Text text = adaptedReader.getCurrentValue();
			MetricString metricString = new MetricString(text.getBytes());
			return (V) new MetricWritable<MetricString>(metricString, MediaType.TEXT_PLAIN);
		}

		@Override
		public float getProgress() throws IOException, InterruptedException {
			return adaptedReader.getProgress();
		}

		@Override
		public void close() throws IOException {
		}
	}

	/**
	 * The implementing table output format.
	 */
	private final HttpInputFormat adapted = new HttpInputFormat();

	
	public HttpInputAdapter() {
		
	}
	
	public HttpInputAdapter(Configuration conf) {
		adapted.setConf(conf);
	}
	
	@Override
	public void setConf(Configuration conf) {
		adapted.setConf(conf);
	}

	@Override
	public Configuration getConf() {
		return adapted.getConf();
	}

	@Override
	public List<InputSplit> getSplits(JobContext context) throws IOException,
			InterruptedException {
		return adapted.getSplits(context);
	}

	@Override
	public RecordReader<K, V> createRecordReader(
			InputSplit split, TaskAttemptContext context) throws IOException,
			InterruptedException {
		return new AdaptedRecordReader(adapted.createRecordReader(split,
				context));
	}
}