package com.simple.original.security;

import com.simple.original.api.exceptions.SimpleException;

public class AuthenticationException extends SimpleException {
    
    /**
     * 
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
