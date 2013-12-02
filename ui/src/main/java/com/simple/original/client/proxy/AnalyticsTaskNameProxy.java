package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.simple.domain.AnalyticsTaskName;

@ProxyFor(value = AnalyticsTaskName.class)
public interface AnalyticsTaskNameProxy extends ValueProxy {

    
    public abstract Long getId();
    
    public abstract String getName();
    
}
