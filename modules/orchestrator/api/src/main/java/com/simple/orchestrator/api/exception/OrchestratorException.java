package com.simple.orchestrator.api.exception;

public class OrchestratorException extends Exception {

	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = -3982963995864133214L;

	/**
	 * 
	 */
	public OrchestratorException() {
		super();
		
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public OrchestratorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	/**
	 * @param message
	 * @param cause
	 */
	public OrchestratorException(String message, Throwable cause) {
		super(message, cause);
		
	}

	/**
	 * @param message
	 */
	public OrchestratorException(String message) {
		super(message);
		
	}

	/**
	 * @param cause
	 */
	public OrchestratorException(Throwable cause) {
		super(cause);
		
	}

}
