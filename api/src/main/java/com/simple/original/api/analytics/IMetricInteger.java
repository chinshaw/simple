package com.simple.original.api.analytics;


public interface IMetricInteger extends IMetricNumber {

    public abstract Integer getValue();

    public abstract void setValue(Integer value);

}