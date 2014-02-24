package com.simple.engine.service.hadoop.mrv1;

public class ConfigurationException extends Exception {

    /**
     * Serializaiton Id
     */
    private static final long serialVersionUID = 4215913327060651284L;

    public ConfigurationException() {
        super();
    }

    public ConfigurationException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(Throwable cause) {
        super(cause);
    }

}
