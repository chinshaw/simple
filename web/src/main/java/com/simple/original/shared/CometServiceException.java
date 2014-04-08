package com.simple.original.shared;

import com.simple.api.exceptions.SimpleException;



public class CometServiceException extends SimpleException {

    /**
     * Default serialization id.
     */
    private static final long serialVersionUID = 1L;
    
    
    public CometServiceException(String message) {
        super(message);
    }
    
    public CometServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    
   
}
