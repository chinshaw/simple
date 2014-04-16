package com.simple.original.client.dashboard;

import java.util.List;

import com.simple.original.client.proxy.AnalyticsOperationOutputProxy;

public interface IProvidesAnalyticsOperationOutputs {

    public List<AnalyticsOperationOutputProxy> getOutputs();
    
}
