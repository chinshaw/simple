package com.simple.original.api.analytics;


public interface IMetricDouble extends IMetricNumber {

    public abstract Double getValue();

    public abstract void setValue(Double value);
}