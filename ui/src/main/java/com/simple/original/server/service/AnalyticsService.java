package com.simple.original.server.service;

import com.simple.domain.model.AnalyticsOperation;
import com.simple.engine.service.AnalyticsOperationException;
import com.simple.engine.service.hadoop.OperationExecutor;

public class AnalyticsService {

	OperationExecutor executor = new OperationExecutor();
	
	public void lintRScript(String script) {
		
	}
	
	public void executeOperation(AnalyticsOperation operation) throws AnalyticsOperationException {
		System.out.println("GOT YOUR REQUESTION OCNTEXT");
		executor.execute(null, operation, null);
	}
}
