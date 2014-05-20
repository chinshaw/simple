package com.simple.orchestrator.api.exception;

public class ReducerException extends RuntimeException {

	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = -1574657921695997322L;

	/**
	 * 
	 */
	public ReducerException() {
		
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ReducerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ReducerException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ReducerException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ReducerException(Throwable cause) {
		super(cause);
	}

}
