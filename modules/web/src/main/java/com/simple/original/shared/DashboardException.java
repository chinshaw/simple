package com.simple.original.shared;

public class DashboardException extends Exception {

	/**
	 * Serialization id
	 */
	private static final long serialVersionUID = 3096191605081178811L;

	public DashboardException() {
	}

	public DashboardException(String message) {
		super(message);
	}

	public DashboardException(Throwable cause) {
		super(cause);
	}

	public DashboardException(String message, Throwable cause) {
		super(message, cause);
	}

}
