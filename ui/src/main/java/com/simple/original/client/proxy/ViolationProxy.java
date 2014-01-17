package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.simple.domain.model.metric.MetricNumber;
import com.simple.domain.model.metric.Violation;
import com.simple.original.api.analytics.IViolation.Severity;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;

/**
 * Base proxy that all other metric proxies extend. It is used mainly for it's inheritence
 * with the RequestFactory. It's concrete implementation is {@link MetricNumber}.
 * @author chinshaw
 */
@ProxyFor(value = Violation.class, locator = RequestFactoryEntityLocator.class)
public interface ViolationProxy extends ValueProxy {
    
	public Severity getSeverity();

    public void setSeverity(Severity severity);

    public String getDetail();

    public void setDetail(String detail);

    public String getRuleName();

    public void setRuleName(String rule);

    public Integer getSubgroup();

    public void setSubgroup(Integer subgroup);
}
