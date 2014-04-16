package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;

@ProxyFor(value = RAnalyticsOperation.class, locator = RequestFactoryEntityLocator.class)
public interface RAnalyticsOperationProxy extends AnalyticsOperationProxy {

	public static String[] EDIT_PROPERTIES = { "rcode" };

	public String getCode();

	public void setCode(String code);
}