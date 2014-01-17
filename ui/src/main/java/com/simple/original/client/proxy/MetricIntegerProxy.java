package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.model.metric.MetricInteger;
import com.simple.original.server.service.locators.IMetricLocator;

@ProxyFor(value = MetricInteger.class, locator = IMetricLocator.class)
public interface MetricIntegerProxy extends MetricNumberProxy {

    public static final String NAME = "Metric Integer";

    public static String[] PROPERTIES = { "ranges" };

    public Integer getValue();
}
