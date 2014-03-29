package com.simple.engine.service.hadoop.io.adapter;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.mapreduce.InputFormat;

import com.simple.api.orchestrator.IMetricKey;
import com.simple.orchestrator.api.IMetricWritable;

public abstract class AbstractInputFormatAdapter<K extends IMetricKey, V extends IMetricWritable>
		extends InputFormat<K, V> implements Configurable {

}
