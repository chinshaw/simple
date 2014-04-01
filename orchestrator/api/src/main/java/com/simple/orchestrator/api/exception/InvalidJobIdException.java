package com.simple.orchestrator.api.exception;

public class InvalidJobIdException extends Exception {


	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = -3495124635747862795L;

	/**
	 * 
	 */
	public InvalidJobIdException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public InvalidJobIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidJobIdException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public InvalidJobIdException(String message) {
		super(message);

	}

	/**
	 * @param cause
	 */
	public InvalidJobIdException(Throwable cause) {
		super(cause);

	}

}
