package com.simple.original.api.orchestrator;

import java.io.Serializable;

public interface INumberRange extends Serializable {

    /**
     * <p>
     * Returns the minimum number in this range.
     * </p>
     * 
     * @return the minimum number in this range
     */
    public abstract Double getMinimum();

    /**
     * <p>
     * Sets the minimum <code>number</code> in the range.
     * </p>
     * 
     * @param min
     *            the minimum value for the range.
     */
    public abstract void setMinimum(Double min);

    /**
     * <p>
     * Returns the maximum number in this range.
     * </p>
     * 
     * @return the maximum number in this range
     */
    public abstract Double getMaximum();

    /**
     * <p>
     * Setter for the maximum value.
     * </p>
     * 
     * @param max
     *            the maximum number for the range
     */
    public abstract void setMaximum(Double max);

    public abstract Criticality getCriticality();

    public abstract void setCriticality(Criticality criticality);

}