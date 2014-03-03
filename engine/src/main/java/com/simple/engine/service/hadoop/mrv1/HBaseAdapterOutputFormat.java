package com.simple.engine.service.hadoop.mrv1;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.log4j.Logger;

public class HBaseAdapterOutputFormat<K extends IMetricKey, V extends IMetricWritable>
		extends OutputFormatAdapter<K, V> {

	private static final Logger logger = Logger
			.getLogger(HBaseAdapterOutputFormat.class.getName());

	public class HbaseRecordWriterAdapter extends RecordWriter<K, V> {

		private final RecordWriter<IMetricKey, Mutation> nativeWriter;

		public HbaseRecordWriterAdapter(
				RecordWriter<IMetricKey, Mutation> nativeWriter) {
			this.nativeWriter = nativeWriter;
			logger.info("Initializing hbase record adapter, family => "
					+ family + " qualifier => " + qualifier);
		}

		/**
		 * Get the configuration and configure the hbase output to write to the
		 * correct column and
		 */
		protected void setup(Context context) throws IOException,
				InterruptedException {
			String column = context.getConfiguration().get("conf.column");
			byte[][] colKey = KeyValue.parseColumn(Bytes.toBytes(column));
			family = colKey[0];
			logger.info("Family ***********************" + new String(family));
			if (colKey.length > 1) {
				qualifier = colKey[1];
			}
		}

		@Override
		public void write(K key, V value) throws IOException,
				InterruptedException {
			Put put = new Put(key.toBytes());
			put.add(family, qualifier, value.toBytes());
			nativeWriter.write(key, put);
		}

		@Override
		public void close(TaskAttemptContext context) throws IOException,
				InterruptedException {
			nativeWriter.close(context);
		}
	}
	
	private TableOutputFormat<IMetricKey> backing_ = new TableOutputFormat<IMetricKey>();

	private byte[] family;
	
	private byte[] qualifier;
	
	@Override
	public RecordWriter<K, V> getRecordWriter(TaskAttemptContext context)
			throws IOException, InterruptedException {
		return new HbaseRecordWriterAdapter(backing_.getRecordWriter(context));
	}

	
	@Override
	public void setConf(Configuration conf) {
		backing_.setConf(conf);
		
		String column = conf.get("conf.column");
		byte[][] colKey = KeyValue.parseColumn(Bytes.toBytes(column));
		family = colKey[0];
		logger.info("Family ***********************" + new String(family));
		if (colKey.length > 1) {
			qualifier = colKey[1];
		}
		
		super.setConf(conf);
	}
	
	@Override
	public void checkOutputSpecs(JobContext context) throws IOException,
			InterruptedException {
		backing_.checkOutputSpecs(context);

	}

	@Override
	public OutputCommitter getOutputCommitter(TaskAttemptContext context)
			throws IOException, InterruptedException {
		return backing_.getOutputCommitter(context);
	}
}
