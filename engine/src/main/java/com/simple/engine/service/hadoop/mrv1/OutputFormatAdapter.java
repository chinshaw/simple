package com.simple.engine.service.hadoop.mrv1;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.OutputFormat;

import com.simple.engine.metric.IMetricKey;

public abstract class OutputFormatAdapter<K extends IMetricKey, V extends IMetricWritable>
		extends OutputFormat<K, V> implements Configurable {

	private Configuration conf_;

	/** Set the configuration to be used by this object. */
	public void setConf(Configuration conf) {
		this.conf_ = conf;
	}

	/** Return the configuration used by this object. */
	public Configuration getConf() {
		return conf_;
	}
}
