package com.artisan.orchestrator.hadoop.job.io.format;

import java.io.IOException;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.artisan.orchestrator.hadoop.job.io.adapter.AbstractOutputFormatAdapter;
import com.artisan.orchestrator.hadoop.job.io.adapter.MetricAdapterFactory;
import com.artisan.orchestrator.server.api.IMetricWritable;
import com.simple.api.orchestrator.IMetricKey;

/**
 * This class is a generic output adapter that will create an underlying output adapter based
 * on the {@link Configuration} output adapter type. The configuration requires a type with the
 * {@link MetricOutputFormat#OUTPUT_ADAPTER_TYPE} and a value of NONE,LOCAL_FILE, HBASE, CASSANDRA, etc in
 * order to write the metrics to the correct location.
 * 
 * @author chinshaw
 *
 * @param <K>
 * @param <V>
 */
public class MetricOutputFormat<K extends IMetricKey, V extends IMetricWritable>
		extends OutputFormat<K, V> implements Configurable {

	private static final Logger logger = Logger.getLogger(MetricOutputFormat.class);
	
	public static final String OUTPUT_ADAPTER_TYPE = "com.simple.engine.service.hadoop.io.adapter.output.type";
	
	public enum OutputAdapterType {
		NONE,
		LOCAL_FILE,
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
		logger.log(Level.FATAL, "Got adapter " + adapter);
	}

	@Override
	public Configuration getConf() {
		return conf_;
	}
	
	public static final void setOutputAdatperType(Configuration configuration, OutputAdapterType type) {
		configuration.setEnum(OUTPUT_ADAPTER_TYPE, type);
	}
}
