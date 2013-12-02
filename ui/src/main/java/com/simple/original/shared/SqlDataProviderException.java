package com.simple.original.shared;

import com.simple.original.api.exceptions.SimpleException;

public class SqlDataProviderException extends SimpleException {

    /**
     * 
     */
    private static final long serialVersionUID = 6319574605039718551L;

    
    public SqlDataProviderException() {
        super();
    }
    
    public SqlDataProviderException(String message) {
        super(message);
    }
    
    public SqlDataProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
