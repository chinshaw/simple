package com.artisan.orchestrator.hadoop.job.io.adapter;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.mapreduce.InputFormat;

import com.artisan.orchestrator.server.api.IMetricWritable;
import com.simple.api.orchestrator.IMetricKey;

public abstract class AbstractInputFormatAdapter<K extends IMetricKey, V extends IMetricWritable>
		extends InputFormat<K, V> implements Configurable {

}
