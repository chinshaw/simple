package com.simple.original.api.orchestrator;



public interface IStringInput extends IAnalyticsOperationInput {

    public void setValue(String value);
    
    public String getValue();    
}