package com.simple.orchestrator.api;

import java.util.Collection;

import org.apache.hadoop.metrics2.annotation.Metric;



public interface IOperationExecutionResponse {

	public Collection<Metric> getMetrics();
}
