package com.simple.original.api.exceptions;




public class AnalyticsTaskException extends SimpleException {

    /**
     * Default serialization id.
     */
    private static final long serialVersionUID = 1L;
    
    private String analyticsJobLog = "";
    
    private String logFileName = "";
    
    
    public AnalyticsTaskException(String message) {
        super(message);
    }
    
    public AnalyticsTaskException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public AnalyticsTaskException(String message, String analyticsJobLog) {
        super(message);
        this.analyticsJobLog = analyticsJobLog;
    }
    
    public AnalyticsTaskException(String message, String analyticsJobLog, Throwable cause) {
        super(message, cause);
        this.analyticsJobLog = analyticsJobLog;
    }
    
    public String getJobLogOutput() {
        return analyticsJobLog;
    }
    
    public String getLogFileName() {
        return logFileName;
    }
    
    public void setLogFileName(String fileName) {
        this.logFileName = fileName;
    }
}
