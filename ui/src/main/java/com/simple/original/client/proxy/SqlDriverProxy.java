package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.simple.domain.SqlDriver;

@ProxyFor(SqlDriver.class)
public interface SqlDriverProxy extends ValueProxy {

    public String getDriverName();
}
