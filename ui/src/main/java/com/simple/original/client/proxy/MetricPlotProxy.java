package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.model.metric.MetricPlot;
import com.simple.original.server.service.locators.IMetricLocator;

@ProxyFor(value = MetricPlot.class, locator = IMetricLocator.class)
public interface MetricPlotProxy extends MetricProxy {

    /**
     * UI Name of the object.
     */
    public String NAME = "Static Plot";
    
    public String getPlotUrl();
}
