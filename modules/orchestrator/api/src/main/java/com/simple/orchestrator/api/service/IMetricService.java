package com.simple.orchestrator.api.service;

import java.util.Collection;

import com.simple.api.orchestrator.IMetric;

public interface IMetricService {
	
	public static final String REST_BASE_URL = "metric";
	
	public Collection<IMetric> find(Long operationId);
	
	public IMetric find(String rowKey);
	
	public IMetric find(String rowKey, String column, String qualifier);
}
