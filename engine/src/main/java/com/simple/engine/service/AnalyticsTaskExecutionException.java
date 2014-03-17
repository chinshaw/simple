package com.simple.engine.service;

import com.simple.original.api.orchestrator.ITaskExecution;

public class AnalyticsTaskExecutionException extends Exception {

	
	/**
	 * Serialization Id.
	 */
	private static final long serialVersionUID = 4759242697529328545L;
	
	private ITaskExecution taskExecution;
	
	public AnalyticsTaskExecutionException() {
		super();
	}
	
	public AnalyticsTaskExecutionException(String message) {
		super(message);
	}
	
	public AnalyticsTaskExecutionException(ITaskExecution taskExecution) {
		this.taskExecution = taskExecution;
	}

	public AnalyticsTaskExecutionException(ITaskExecution taskExecution, Throwable cause) {
		taskExecution.createFailure(cause);
	}
	
	public ITaskExecution getTaskExecution() {
		return taskExecution;
	}
	
}
