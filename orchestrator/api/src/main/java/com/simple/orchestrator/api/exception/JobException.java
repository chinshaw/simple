package com.simple.orchestrator.api.exception;


public class JobException extends OrchestratorException {

	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = -7958821950717812139L;

	/**
	 * 
	 */
	public JobException() {
		super();

	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public JobException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

	/**
	 * @param message
	 * @param cause
	 */
	public JobException(String message, Throwable cause) {
		super(message, cause);

	}

	/**
	 * @param message
	 */
	public JobException(String message) {
		super(message);

	}

	/**
	 * @param cause
	 */
	public JobException(Throwable cause) {
		super(cause);
	}

}
