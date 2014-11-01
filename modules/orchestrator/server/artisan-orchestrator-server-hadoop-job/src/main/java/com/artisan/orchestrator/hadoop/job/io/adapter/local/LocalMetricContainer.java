package com.artisan.orchestrator.hadoop.job.io.adapter.local;

import java.io.Serializable;

import com.artisan.orchestrator.server.api.IMetricWritable;
import com.simple.api.orchestrator.IMetricKey;

/**
 * This is a simple object that is used to wrap a metric and
 * it's key. This can be converted to json in order to write it
 * to a file.
 * @author chinshaw
 *
 */

public class LocalMetricContainer implements Serializable {

	private static final long serialVersionUID = -7369888982039006898L;

	private IMetricKey key;
	
	private IMetricWritable metric;

	public LocalMetricContainer() {
	}
	
	public LocalMetricContainer(IMetricKey key, IMetricWritable metric) {
		this.key = key;
		this.metric = metric;
	}

	public IMetricKey getKey() {
		return key;
	}

	public void setKey(IMetricKey key) {
		this.key = key;
	}

	public IMetricWritable getMetric() {
		return metric;
	}

	public void setMetric(IMetricWritable metric) {
		this.metric = metric;
	}
	
	
}
