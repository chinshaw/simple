package com.simple.orchestrator.api.exception;


public class HadoopJobException extends OrchestratorException {

	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = -7958821950717812139L;

	/**
	 * 
	 */
	public HadoopJobException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public HadoopJobException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

	/**
	 * @param message
	 * @param cause
	 */
	public HadoopJobException(String message, Throwable cause) {
		super(message, cause);

	}

	/**
	 * @param message
	 */
	public HadoopJobException(String message) {
		super(message);

	}

	/**
	 * @param cause
	 */
	public HadoopJobException(Throwable cause) {
		super(cause);
	}

}
