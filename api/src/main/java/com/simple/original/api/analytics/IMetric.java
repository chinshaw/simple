package com.simple.original.api.analytics;


public interface IMetric extends IHasViolations {


	/**
	 * Id if the metric
	 * @return
	 */
	public Long getId();

	/**
	 * Set the name of the object.
	 * 
	 * @param name
	 */
	public abstract void setName(String name);

	/**
	 * Getter for the name of the object.
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * Returns true if metric has violations.
	 * 
	 * @return
	 */
	public abstract boolean hasMetricViolations();
}