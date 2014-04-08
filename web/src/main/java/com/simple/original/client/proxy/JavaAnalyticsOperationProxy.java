package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.model.JavaAnalyticsOperation;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;

@ProxyFor(value = JavaAnalyticsOperation.class, locator = RequestFactoryEntityLocator.class)
public interface JavaAnalyticsOperationProxy extends AnalyticsOperationProxy {

}
