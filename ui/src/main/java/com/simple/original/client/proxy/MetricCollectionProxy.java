/**
 * 
 */
package com.simple.original.client.proxy;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.model.metric.MetricCollection;
import com.simple.original.server.service.locators.IMetricLocator;

/**
 * @author chinshaw
 * 
 */
@ProxyFor(value = MetricCollection.class, locator = IMetricLocator.class)
public interface MetricCollectionProxy extends MetricProxy {

    public static String NAME = "Metric Collection";

    public static String[] PROPERTIES = { "value" };

    public static String[] DATAFRAME_PROPERTIES = { "value", "value.value", "value.value.totalRange", "value.value.lowRange", "value.value.midRange", "value.value.highRange" };

    public List<MetricProxy> getValue();
}