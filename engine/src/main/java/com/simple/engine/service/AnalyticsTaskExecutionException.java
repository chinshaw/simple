package com.simple.engine.service;

import com.simple.domain.AnalyticsTaskExecution;

public class AnalyticsTaskExecutionException extends Exception {

	
	/**
	 * Serialization Id.
	 */
	private static final long serialVersionUID = 4759242697529328545L;
	
	private AnalyticsTaskExecution taskExecution;
	
	public AnalyticsTaskExecutionException() {
		super();
	}
	
	public AnalyticsTaskExecutionException(String message) {
		super(message);
	}
	
	public AnalyticsTaskExecutionException(AnalyticsTaskExecution taskExecution) {
		this.taskExecution = taskExecution;
	}

	public AnalyticsTaskExecutionException(AnalyticsTaskExecution taskExecution, Throwable cause) {
		taskExecution.setFailure(cause);
	}
	
	public AnalyticsTaskExecution getTaskExecution() {
		return taskExecution;
	}
	
}
