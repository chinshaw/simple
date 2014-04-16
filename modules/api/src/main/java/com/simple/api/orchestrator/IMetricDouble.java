package com.simple.api.orchestrator;


public interface IMetricDouble extends IMetricNumber {

    public abstract Double getValue();

    public abstract void setValue(Double value);
}