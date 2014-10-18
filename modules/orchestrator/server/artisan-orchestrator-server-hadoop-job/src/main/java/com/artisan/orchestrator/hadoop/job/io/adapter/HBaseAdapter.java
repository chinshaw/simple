package com.artisan.orchestrator.hadoop.job.io.adapter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.hadoop.conf.Configurable;
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

import com.artisan.orchestrator.hadoop.job.io.MetricWritable;
import com.simple.api.orchestrator.IMetricKey;
import com.simple.orchestrator.api.IMetricWritable;

public class HBaseAdapter<K extends IMetricKey, V extends IMetricWritable>
		extends AbstractOutputFormatAdapter<K, V> implements Configurable {
	
	public static String COLUMN_VALUE_KEY = "com.artisan.orchestrator.hbase.metric.colvalue";
	
	public static String COLUMN_CLASS_KEY = "com.artisan.orchestrator.hbase.metric.colclass";

	public static String COLUMN_MEDIATYPE = "com.artisan.orchestrator.hbase.metric.colmediatype";

	private static final Logger logger = Logger.getLogger(HBaseAdapter.class
			.getName());

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
			if (colKey.length > 1) {
				qualifier = colKey[1];
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * This is where we wrap the call to write the metric out to the hbase
		 * file system. We write both the value and the mime type so that it is
		 * easy to re convert it.
		 * 
		 * To convert the metricwritable to binary we use the {@link MetricWritable#write(java.io.DataOutput)} 
		 * to write the output to a byte stream. This is important because it will write the size of the
		 * byte stream along with the class of the bytes stream. When un marshalling the
		 * byte stream it is required that you read the size and then the class or simply use the MetricWritable
		 * readFields to unserialize the binary stream.
		 */
		@Override
		public void write(K key, V value) throws IOException,
				InterruptedException {
			
			// Configure the key in the put
			Put put = new Put(key.toBytes());
			

			ByteArrayOutputStream valueStream = new ByteArrayOutputStream(4096);
			DataOutputStream daos = new DataOutputStream(valueStream);
			value.write(daos);
			
			put.add(family, Bytes.toBytes(getColumnFamily()),
					valueStream.toByteArray());
			
			put.add(family, Bytes.toBytes(getColumnFamily()),
					Bytes.toBytes(value.getClass().getName()));
			
			put.add(family, Bytes.toBytes(COLUMN_MEDIATYPE), value
					.getMimeType().getBytes());
			nativeWriter.write(key, put);
		}

		@Override
		public void close(TaskAttemptContext context) throws IOException,
				InterruptedException {
			nativeWriter.close(context);
		}
	}

	/**
	 * The implementing table output format.
	 */
	private TableOutputFormat<IMetricKey> adapted = new TableOutputFormat<IMetricKey>();

	/**
	 * This is the table family that owns all the columns.
	 */
	private byte[] family;

	/**
	 * This is the column qualifier that belongs to the table family.
	 */
	private byte[] qualifier;

	public HBaseAdapter() {
	}
	
	public HBaseAdapter(Configuration conf) {
		setConf(conf);
	}

	@Override
	public RecordWriter<K, V> getRecordWriter(TaskAttemptContext context)
			throws IOException, InterruptedException {
		return new HbaseRecordWriterAdapter(adapted.getRecordWriter(context));
	}

	/**
	 * Overridden to set the configuration on the backing TableOutputFormat and
	 */
	@Override
	public void setConf(Configuration conf) {
		adapted.setConf(conf);

		String column = conf.get("conf.column");
		byte[][] colKey = KeyValue.parseColumn(Bytes.toBytes(column));
		family = colKey[0];
		if (colKey.length > 1) {
			qualifier = colKey[1];
		}

		super.setConf(conf);
	}

	/**
	 * Calls {@link TableOutputFormat#checkOutputSpecs(JobContext)}
	 */
	@Override
	public void checkOutputSpecs(JobContext context) throws IOException,
			InterruptedException {
		adapted.checkOutputSpecs(context);
	}

	/**
	 * Calls {@link TableOutputFormat#getOutputCommitter(TaskAttemptContext)}
	 */
	@Override
	public OutputCommitter getOutputCommitter(TaskAttemptContext context)
			throws IOException, InterruptedException {
		return adapted.getOutputCommitter(context);
	}
	
	public String getColumnFamily() {
		return getConf().get(COLUMN_CLASS_KEY, "metrics");
	}
}
