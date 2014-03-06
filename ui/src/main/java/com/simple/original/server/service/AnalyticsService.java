package com.simple.original.server.service;

import com.simple.domain.model.AnalyticsOperation;
import com.simple.engine.service.AnalyticsOperationException;
import com.simple.engine.service.hadoop.config.ConfigurationException;
import com.simple.engine.service.hadoop.mrv1.OperationDriver;

public class AnalyticsService {

	OperationDriver executor = new OperationDriver();
	
	public void lintRScript(String script) {
		
	}
	
	public void executeOperation(AnalyticsOperation operation) throws AnalyticsOperationException {
		try {
			executor.execute(null, operation, null);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
