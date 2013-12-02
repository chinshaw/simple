package com.simple.original.client.proxy;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.metric.MetricNumber;
import com.simple.original.server.service.locators.IMetricLocator;

@ProxyFor(value = MetricNumber.class, locator = IMetricLocator.class)
public interface MetricNumberProxy extends MetricProxy {
    
    public List<NumberRangeProxy> getRanges();
    
    public void setRanges(List<NumberRangeProxy> ranges);
}