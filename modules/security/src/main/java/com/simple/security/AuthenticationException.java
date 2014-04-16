package com.simple.security;

import com.simple.api.exceptions.SimpleException;

public class AuthenticationException extends SimpleException {
    
    /**
     * Serialization Id
     */
    private static final long serialVersionUID = 1L;

    public AuthenticationException() {
        super();
    }
    
    public AuthenticationException(String message) {
        super(message);
    }
    
    public AuthenticationException(String message, Throwable throwable) {
    	super(message, throwable);
    }

}
