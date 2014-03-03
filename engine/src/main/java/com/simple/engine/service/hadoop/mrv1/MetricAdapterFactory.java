package com.simple.engine.service.hadoop.mrv1;

import org.apache.hadoop.conf.Configuration;

public class MetricAdapterFactory {

	
	public static <K extends IMetricKey,V extends IMetricWritable> OutputFormatAdapter<K,V> createAdapter(Configuration conf) {
		OutputFormatAdapter<K,V> outputFormatAdapter = new HBaseAdapterOutputFormat<K, V>();
		outputFormatAdapter.setConf(conf);
		return outputFormatAdapter;
	}
}
