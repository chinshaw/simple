package com.simple.original.api.analytics;

import java.io.Serializable;

public interface IMetric extends IHasViolations, Serializable {

	
	/**
	 * Id if the metric
	 * @return
	 */
	public Long getId();
	
	
    /**
     * Getter for the name of the object.
     * 
     * @return
     */
    public abstract String getName();

    /**
     * Set the name of the object.
     * 
     * @param name
     */
    public abstract void setName(String name);

    /**
     * Returns true if metric has violations.
     * 
     * @return
     */
    public abstract boolean hasMetricViolations();
	
	public abstract byte[] toBytes();

}