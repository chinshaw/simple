package com.simple.domain.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import com.simple.domain.model.metric.Metric;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.original.api.analytics.IAnalyticsTaskExecution;
import com.simple.original.api.analytics.IMetric;
import com.simple.original.api.exceptions.ExceptionUtils;

@Entity
@NamedQueries( value = {
		@NamedQuery(name="AnalyticsTaskExecution.countByTaskId", query = "select count(exec) from AnalyticsTaskExecution as exec where exec.analyticsTask.id = :taskId")
})
public class AnalyticsTaskExecution extends RequestFactoryEntity implements IAnalyticsTaskExecution {

    /**
     * Serialization id.
     */
    private static transient final long serialVersionUID = 356227840389171598L;

    private boolean interactive = false;

	/**
     * This is the date that the analytics task was executed.
     */
    private Date startTime;

    /**
     * Time the task was completed. This will be set even if task was not
     * successful.
     */
    private Date endTime;

    /**
     * Completion status of the task.
     */
    @Enumerated(EnumType.ORDINAL)
    private TaskCompletionStatus completionStatus;

    /**
     * This is the execution log which is typically transient on the object. We
     * will use a prepersist method to write out the execution log to file so
     * that we can retrieve it quickly.
     */
    @Transient
    private transient String executionLog;

    private String executionLogFileName;

    /**
     * The link to the task that was executed.
     */
    @ManyToOne(optional = false)
    private AnalyticsTask analyticsTask;

    /**
     * These are the outputs for the analytics task.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = Metric.class)
    private List<IMetric> executionMetrics = new ArrayList<IMetric>();

    /**
     * These are the inputs that were used during execution. They are saved for
     * posterity.
     */
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, targetEntity = AnalyticsOperationInput.class)
    private List<AnalyticsOperationInput> analyticsTaskInputs = new ArrayList<AnalyticsOperationInput>();

    /**
     * Default constructor.
     */
    public AnalyticsTaskExecution() {
    }
    
    /**
     * Constructor that will set the analytics task for the object.
     * @param analyticsTask
     */
    public AnalyticsTaskExecution(AnalyticsTask analyticsTask) {
        this.analyticsTask = analyticsTask;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.simple.original.server.domain.IAnalyticsTaskExecutionEntity#addExecutionMetric
     * (com.simple.original.server.domain.IMetricEntity)
     */
    public void addExecutionMetric(Metric metric) {
        executionMetrics.add(metric);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.simple.original.server.domain.IAnalyticsTaskExecutionEntity#getExecutionMetrics
     * ()
     */
    @Override
    public List<IMetric> getExecutionMetrics() {
        return executionMetrics;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.simple.original.server.domain.IAnalyticsTaskExecutionEntity#getCompletionTime()
     */
    @Override
    public Date getCompletionTime() {
        return endTime;
    }

    public void setCompletionTime(Date endTime) {
        this.endTime = endTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.simple.original.server.domain.IAnalyticsTaskExecutionEntity#getAnalyticsTask()
     */
    @Override
    public AnalyticsTask getAnalyticsTask() {
        return analyticsTask;
    }
    
    public void setAnalyticsTask(AnalyticsTask analyticsTask) {
        this.analyticsTask = analyticsTask;
    }
    
    public Long getAnalyticsTaskId() {
        if (analyticsTask != null) {
            return analyticsTask.getId();
        }
        return null;
    }

    public String toDebugString() {
        String debugString = "";
        debugString += "Created on " + getStartTime() + "\n";
        for (IMetric output : executionMetrics) {
            debugString += output.toString();
        }

        return debugString;
    }

    public void setCompletionStatus(TaskCompletionStatus completionStatus) {
        this.completionStatus = completionStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.simple.original.server.domain.IAnalyticsTaskExecutionEntity#getCompletionStatus
     * ()
     */
    @Override
    public TaskCompletionStatus getCompletionStatus() {
        return this.completionStatus;
    }

    /**
     * Setter for the analtyics task executions.
     * 
     * @param analyticsTaskInputs
    public void setAnalyticsTaskInputs(List<AnalyticsOperationInput> analyticsTaskInputs) {
        this.analyticsTaskInputs = analyticsTaskInputs;
    }     
    */

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.simple.original.server.domain.IAnalyticsTaskExecutionEntity#getAnalyticsTaskInputs
     * ()
     */
    public List<AnalyticsOperationInput> getAnalyticsTaskInputs() {
        return analyticsTaskInputs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.simple.original.server.domain.IAnalyticsTaskExecutionEntity#addAnalyticsTaskInputs
     * (java.util.List)

    public void addAnalyticsTaskInputs(List<AnalyticsOperationInput> inputs) {
        if (this.analyticsTaskInputs == null) {
            this.analyticsTaskInputs = new ArrayList<AnalyticsOperationInput>();
        }
        this.analyticsTaskInputs.addAll(inputs);
    }
         */

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.simple.original.server.domain.IAnalyticsTaskExecutionEntity#addAnalyticsTaskInput
     * (com.simple.original.server.domain.AnalyticsOperationInput)

    public void addAnalyticsTaskInput(AnalyticsOperationInput input) {
        if (this.analyticsTaskInputs == null) {
            this.analyticsTaskInputs = new ArrayList<AnalyticsOperationInput>();
        }

        this.analyticsTaskInputs.add(input);
    }
    */

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.simple.original.server.domain.IAnalyticsTaskExecutionEntity#getExecutionLog()
     */
    @Override
    public String getExecutionLog() {
        if (executionLog == null && executionLogFileName != null) {
            try {
                executionLog = readLogFile();
            } catch (IOException e) {
                Logger.getLogger(AnalyticsTaskExecution.class.getName()).log(Level.SEVERE, "Unable to read execution log file " + getExecutionLogFileName(), e);
            }
        }

        return executionLog;
    }

    public void setExecutionLog(String executionLog) {
        this.executionLog = executionLog;
    }

    @PrePersist
    public void writeLogFile() throws IOException {
        if (executionLog != null && !executionLog.isEmpty()) {
            String fileName = writeLogFile(getExecutionLog());
            setExecutionLogFileName(fileName);
        }
    }

    public static String writeLogFile(String executionLog) throws IOException {
        if (executionLog == null) {
            throw new IllegalArgumentException("executionLog cannot be null");
        }
        if (executionLog.isEmpty()) {
            throw new IllegalArgumentException("executionLog was empty so nothing to write");
        }

        String fileName = UUID.randomUUID().toString() + ".log";
        //String filePath = ServerProperties.getExecutionLogFSPath() + fileName;
        // TODO removed file utils so will have to do this manually.
        //FileUtils.writeStringToFile(new File(filePath), executionLog);
        return fileName;

    }

    private String readLogFile() throws IOException {
        if (executionLogFileName != null) {
            // TODO removed file utils so will have to do this manually.
            // return FileUtils.readFileToString(new File(filePath));
        }
        return null;
    }

    /**
     * Getter for the
     * 
     * @return
     */
    public String getExecutionLogFileName() {
        return this.executionLogFileName;
    }

    private void setExecutionLogFileName(String fileName) {
        this.executionLogFileName = fileName;
    }
    
    
    /**
     * Was this task run interactively? Otherwise it may 
     * have been run from the scheduler. This is here so that we can delete
     * interactive tasks without affecting the scheduled task history.
     * We don't want to show the interactive task history.
     * @return
     */
    public boolean isInteractive() {
		return interactive;
	}

    /**
     * Set whether this task was run interactively or not.
     * @param interactive
     */
	public void setInteractive(boolean interactive) {
		this.interactive = interactive;
	}

	public void setFailure(Throwable cause) {
		this.setCompletionStatus(TaskCompletionStatus.FAILED);
		this.executionLog.concat("SEVERE ERROR\n");
		String stack = ExceptionUtils.stackTraceToString(cause);
		this.executionLog.concat(stack);
	}
}