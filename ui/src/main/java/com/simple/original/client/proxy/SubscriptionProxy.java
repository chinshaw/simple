package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.simple.domain.model.Subscription;

@ProxyFor(value = Subscription.class)
public interface SubscriptionProxy extends ValueProxy {
	
	public enum SubscriptionType {
		ALERT_DEFINITION,REPORT_TASK ;
	}

	public Long getId();
	
	public String getName();

	public String getDescription();

	public boolean isSubscribed();
	
}
