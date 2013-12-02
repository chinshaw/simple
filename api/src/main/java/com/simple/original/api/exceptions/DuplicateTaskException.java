package com.simple.original.api.exceptions;

public class DuplicateTaskException extends RuntimeException {

	/**
	 * Serialization
	 */
	private static final long serialVersionUID = 6165273763954625366L;

	public DuplicateTaskException(String message) {
		super(message);
	}

	public DuplicateTaskException(String message, Throwable cause) {
		super(message, cause);
	}
}
