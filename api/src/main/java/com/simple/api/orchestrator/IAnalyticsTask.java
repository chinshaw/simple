package com.simple.api.orchestrator;

import java.io.Serializable;
import java.util.List;

/**
 * This option extends serializable so that it can be serialized by quartz.
 * 
 * @author chinshaw
 */
public interface IAnalyticsTask extends Serializable {

    /**
     * Getter for name
     * 
     * @return Name of analytics task
     */
    public abstract String getName();

    /**
     * Setter for the name of the task.
     * 
     * @param name
     */
    public abstract void setName(String name);

    /**
     * Text description for analytics task.
     * 
     * @return Text description of analytics task.
     */
    public abstract String getDescription();

    /**
     * Setter for the task description.
     * 
     * @param description
     */
    public abstract void setDescription(String description);

    /**
     * Get the script object of the analytics task. This will contain the code
     * and the actual implementation.
     * 
     * @return
     */
    public abstract List<? extends IAnalyticsOperation> getOperations();
    
    
    public List<? extends IDataProvider> getDataProviders();

}