package com.simple.original.api.analytics;

public interface IDataProvider {

    /**
     * Getter for variable name.
     * @return variable name that will be assigned the value.
     */
    public abstract String getVariableName();

    /**
     * Setter for variable name.
     * @param variableName
     */
    public abstract void setVariableName(String variableName);
}