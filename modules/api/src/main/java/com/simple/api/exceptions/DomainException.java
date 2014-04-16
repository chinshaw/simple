package com.simple.api.exceptions;


/**
 * Entity related exception.
 * 
 * 
 * @author chinshaw
 *
 */
public class DomainException extends Exception {

	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = 8261354306778860323L;

	/**
	 * {@inheritDoc}
	 */
	public DomainException() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	public DomainException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * {@inheritDoc}
	 */
	public DomainException(String message) {
		super(message);
	}

	/**
	 * {@inheritDoc}
	 */
	public DomainException(Throwable throwable) {
		super(throwable);
	}

	
	
}
