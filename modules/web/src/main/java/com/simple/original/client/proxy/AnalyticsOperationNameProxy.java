package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.simple.domain.model.AnalyticsOperationName;

@ProxyFor(value = AnalyticsOperationName.class)
public interface AnalyticsOperationNameProxy extends ValueProxy{

    public Long getId();
    
    public String getName();
}
