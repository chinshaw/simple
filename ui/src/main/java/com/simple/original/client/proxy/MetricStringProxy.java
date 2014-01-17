package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.model.metric.MetricString;
import com.simple.original.server.service.locators.IMetricLocator;

@ProxyFor(value = MetricString.class, locator = IMetricLocator.class)
public interface MetricStringProxy extends MetricProxy {

    public static String NAME = "MetricString";

    public String getValue();

    public void setValue(String value);
}
