package com.simple.api.exceptions;

public class SimpleException extends Exception {

    /**
     * Default serial.
     */
    private static final long serialVersionUID = 1L;

    
    public SimpleException() {
        super();
    }
    
    public SimpleException(String message) {
        super(message);
    }
    
    public SimpleException(String message, Throwable cause) {
        super(message, cause);
    }
}
