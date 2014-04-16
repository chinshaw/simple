package com.simple.original.shared;

import com.simple.api.exceptions.SimpleException;


public class SecurityException extends SimpleException {

    /**
     * 
     */
    private static final long serialVersionUID = 4018263961055786359L;
    
    public SecurityException(String message) {
        super(message);
    }
    
    public SecurityException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
