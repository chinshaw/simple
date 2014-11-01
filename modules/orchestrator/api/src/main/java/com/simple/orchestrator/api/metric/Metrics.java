package com.simple.orchestrator.api.metric;

import java.util.List;

/**
 * Simple container for metrics.
 * @author chinshaw
 *
 */
public class Metrics {

	private List<Metric> metrics;

	public List<Metric> getMetrics() {
		return metrics;
	}

	public void setMetrics(List<Metric> metrics) {
		this.metrics = metrics;
	}
}
