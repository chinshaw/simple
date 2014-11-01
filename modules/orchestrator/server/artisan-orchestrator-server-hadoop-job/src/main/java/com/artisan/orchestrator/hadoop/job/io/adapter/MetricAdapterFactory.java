package com.artisan.orchestrator.hadoop.job.io.adapter;

import org.apache.hadoop.conf.Configuration;

import com.artisan.orchestrator.hadoop.job.io.adapter.hbase.HBaseMetricAdapterFacade;
import com.artisan.orchestrator.hadoop.job.io.format.MetricInputFormat;
import com.artisan.orchestrator.hadoop.job.io.format.MetricOutputFormat;
import com.artisan.orchestrator.hadoop.job.io.format.MetricInputFormat.InputAdapterType;
import com.artisan.orchestrator.hadoop.job.io.format.MetricOutputFormat.OutputAdapterType;
import com.artisan.orchestrator.server.api.IMetricWritable;
import com.simple.api.orchestrator.IMetricKey;

public class MetricAdapterFactory {

	public static <K extends IMetricKey, V extends IMetricWritable> AbstractOutputFormatAdapter<K, V> createOutputAdapter(
			Configuration conf) {
		
		OutputAdapterType type = conf.getEnum(MetricOutputFormat.OUTPUT_ADAPTER_TYPE, OutputAdapterType.NONE);

		switch (type) {
		case HBASE:
			return new HBaseMetricAdapterFacade<K, V>(conf);
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
