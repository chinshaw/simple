package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.model.metric.MetricDouble;
import com.simple.original.server.service.locators.IMetricLocator;

@ProxyFor(value = MetricDouble.class, locator = IMetricLocator.class)
public interface MetricDoubleProxy extends MetricNumberProxy {

    public static final String NAME = "Metric Number";

    public static String[] PROPERTIES = {"ranges"};

    public Double getValue();
}
