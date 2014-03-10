package com.simple.engine.service.hadoop.io.adapter;

import org.apache.hadoop.conf.Configuration;

import com.simple.engine.api.IMetricKey;
import com.simple.engine.service.hadoop.io.IMetricWritable;
import com.simple.engine.service.hadoop.io.format.MetricInputFormat;
import com.simple.engine.service.hadoop.io.format.MetricOutputFormat;
import com.simple.engine.service.hadoop.io.format.MetricInputFormat.InputAdapterType;
import com.simple.engine.service.hadoop.io.format.MetricOutputFormat.OutputAdapterType;

public class MetricAdapterFactory {

	public static <K extends IMetricKey, V extends IMetricWritable> AbstractOutputFormatAdapter<K, V> createOutputAdapter(
			Configuration conf) {
		OutputAdapterType type = conf.getEnum(MetricOutputFormat.OUTPUT_ADAPTER_TYPE, OutputAdapterType.NONE);
		
		switch (type) {
		case HBASE:
			return new HBaseAdapter<K, V>(conf);
		case NONE:
			return null;
		default:
			throw new RuntimeException("Unknown output adapter type");
		
		}
	}
	
	public static <K extends IMetricKey, V extends IMetricWritable> AbstractInputFormatAdapter<K, V> createInputAdapter(
			Configuration conf) {
		InputAdapterType type = conf.getEnum(MetricInputFormat.INPUT_ADAPTER_TYPE, InputAdapterType.NONE);
		switch (type) {
		case HTTP:
			return new HttpInputAdapter<K, V>(conf);
		case NONE:
			return null;
		default:
			throw new RuntimeException("Unknown input adapter type");
		}
	}
}
