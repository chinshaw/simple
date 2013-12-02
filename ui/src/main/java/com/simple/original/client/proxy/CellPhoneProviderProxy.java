package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.simple.domain.CellPhoneProvider;

@ProxyFor(value = CellPhoneProvider.class)
public interface CellPhoneProviderProxy extends ValueProxy {

	public Long getId();

	public void setId(Long id);

	public void setEmailPostfix(String emailPostfix);

	public String getEmailPostfix();

	public void setName(String name);

	public String getName();

}
