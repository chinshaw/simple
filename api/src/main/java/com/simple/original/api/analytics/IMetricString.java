package com.simple.original.api.analytics;



public interface IMetricString extends IMetric {
    
    public abstract String getValue();
    
    public abstract void setValue(String value);
}