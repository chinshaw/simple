package com.simple.api.orchestrator;

import java.util.List;

public interface IMetricCollection extends IMetric {

    public abstract List<? extends IMetric> getValue();

}