package com.simple.radapter.api;

public class ParseException extends RuntimeException {

	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = -6207368949463196992L;

	public ParseException() {
		super();
	}

	public ParseException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ParseException(String message) {
		super(message);
	}

	public ParseException(Throwable throwable) {
		super(throwable);
	}
}
