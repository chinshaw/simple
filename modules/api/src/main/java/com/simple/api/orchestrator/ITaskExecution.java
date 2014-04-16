package com.simple.api.orchestrator;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public interface ITaskExecution extends Serializable {

	public enum TaskCompletionStatus {
	    SUCCESS, FAILED, VIOLATIONS
	}

	
    /**
     * Getter for the log output.
     * 
     * @return
     */
    public abstract String getExecutionLog();

    public abstract Date getStartTime();

    public abstract Date getCompletionTime();
    
    public abstract IAnalyticsTask getAnalyticsTask();

    public abstract TaskCompletionStatus getCompletionStatus();

    public abstract void setCompletionStatus(TaskCompletionStatus completionStatus);

    public abstract List<? extends IMetric> getExecutionMetrics();

	public abstract void createFailure(Throwable cause);

	public abstract Long getId();
}