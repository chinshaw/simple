package com.simple.engine.service;

public class AnalyticsOperationException extends Exception {

	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = -1579988305393825600L;

	public AnalyticsOperationException() {
		super();
	}

	public AnalyticsOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AnalyticsOperationException(String message) {
		super(message);
	}

	public AnalyticsOperationException(Throwable cause) {
		super(cause);
	}
}
