package com.simple.original.api.analytics;



public interface IAnalyticsOperationInput {

    /**
     * Getter for the variable name;
     * 
     * @return
     */
    public abstract String getInputName();

    public abstract String getValueAsString();
}