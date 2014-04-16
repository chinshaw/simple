package com.simple.api.exceptions;



public class RAnalyticsException extends SimpleException {

    /**
     * Default serialization id.
     */
    private static final long serialVersionUID = 1L;
    
    private String analyticsJobLog = "";
    
    
    public RAnalyticsException(String message) {
        super(message);
    }
    
    public RAnalyticsException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public RAnalyticsException(String message, String analyticsJobLog) {
        super(message);
        this.analyticsJobLog = analyticsJobLog;
    }
    
    public RAnalyticsException(String message, String analyticsJobLog, Throwable cause) {
        super(message, cause);
        this.analyticsJobLog = analyticsJobLog;
    }
    
    public String getJobLogOutput() {
        return analyticsJobLog;
    } 
}
