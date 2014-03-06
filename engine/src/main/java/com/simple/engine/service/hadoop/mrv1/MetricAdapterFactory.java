package com.simple.engine.service.hadoop.mrv1;

import org.apache.hadoop.conf.Configuration;

import com.simple.engine.metric.IMetricKey;

public class MetricAdapterFactory {

	
	public static <K extends IMetricKey,V extends IMetricWritable> OutputFormatAdapter<K,V> createAdapter(Configuration conf) {
		OutputFormatAdapter<K,V> outputFormatAdapter = new HBaseAdapter<K, V>();
		outputFormatAdapter.setConf(conf);
		return outputFormatAdapter;
	}
}
