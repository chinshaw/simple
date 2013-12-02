package com.simple.original.api.analytics;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;

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

    /**
     * Getter for data provider. TODO this needs to be converted to an
     * interface.
     * 
     * @return
     */
    @XmlElementWrapper
    @XmlElementRef
    public abstract List<? extends IDataProvider> getDataProviders();
}