package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.model.SqlDataProvider;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;


@ProxyFor(value=SqlDataProvider.class, locator=RequestFactoryEntityLocator.class)
public interface SqlDataProviderProxy extends DataProviderProxy {

	void setSqlStatement(String sql);
	
	String getSqlStatement();
}
