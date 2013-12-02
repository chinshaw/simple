package com.simple.original.api.analytics;

import java.util.List;

public interface IMetricCollection extends IMetric {

    public abstract List<? extends IMetric> getValue();

}