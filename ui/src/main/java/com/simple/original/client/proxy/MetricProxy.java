package com.simple.original.client.proxy;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.model.metric.Metric;
import com.simple.domain.model.metric.MetricNumber;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;

/**
 * Base proxy that all other metric proxies extend. It is used mainly for it's
 * inheritence with the RequestFactory. It's concrete implementation is
 * {@link MetricNumber}.
 * 
 * @author chinshaw
 */
@ProxyFor(value = Metric.class, locator = RequestFactoryEntityLocator.class)
public interface MetricProxy extends DatastoreObjectProxy {

    public Long getId();

    /**
     * Name of this object in the ui. This will commonly be extended by
     * subclasses.
     */
    public String NAME = "Generic Metric";

    /**
     * This is a list of types of outputs that the ui can support. This is
     * typically used for listing types of supported Metrics in the operation
     * output screen.
     * 
     * @author chinshaw
     */
    public enum MetricOutputType {
        MetricNumber, MetricString, MetricCollection, MetricMatrix, MetricStaticChart
    }

    /**
     * See {@link MetricNumber#setName(String)}
     * 
     * @param name
     */
    public void setName(String name);

    /**
     * See {@link MetricNumber#setContext(String)}
     * 
     * @return name of the metric.
     */
    public String getName();

    /**
     * Get the list of violations attached to this metric.
     * 
     * @return
     */
    public List<ViolationProxy> getViolations();
    
}
