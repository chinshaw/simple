package com.simple.api.orchestrator;



public interface IMetricString extends IMetric {
    
    public abstract String getValue();
    
    public abstract void setValue(String value);
}