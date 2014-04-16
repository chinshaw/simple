package com.simple.api.orchestrator;


public interface IMetricInteger extends IMetricNumber {

    public abstract Integer getValue();

    public abstract void setValue(Integer value);

}