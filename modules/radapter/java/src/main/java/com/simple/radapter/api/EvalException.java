package com.simple.radapter.api;

public class EvalException extends RuntimeException {

	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = -6207368949463196992L;

	public EvalException() {
		super();
	}

	public EvalException(String message, Throwable cause) {
		super(message, cause);
	}

	public EvalException(String message) {
		super(message);
	}

	public EvalException(Throwable cause) {
		super(cause);
	}

}
