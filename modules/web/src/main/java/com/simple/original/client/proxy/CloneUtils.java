package com.simple.original.client.proxy;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.requestfactory.shared.RequestContext;

public class CloneUtils {

	
	public static AnalyticsOperationInputProxy clone(RequestContext context, AnalyticsOperationInputProxy origProxy) {
		if (origProxy instanceof UIUserInputModelProxy) {
			return ProxyUtils.cloneProxy(UIUserInputModelProxy.class,(UIUserInputModelProxy) origProxy, context);
		}
		
		if (origProxy instanceof UIDateInputModelProxy) {
			return ProxyUtils.cloneProxy(UIDateInputModelProxy.class,(UIDateInputModelProxy) origProxy, context);
		}
		
		if (origProxy instanceof UIComplexInputModelProxy) {
			return clone(context, (UIComplexInputModelProxy) origProxy);
		}
		
		return null;
	}
	
	
	public static UIComplexInputModelProxy clone(RequestContext context, UIComplexInputModelProxy origProxy) {
		UIComplexInputModelProxy cloneProxy = context.create(UIComplexInputModelProxy.class);
		AutoBean<UIComplexInputModelProxy> autoOrig = AutoBeanUtils.getAutoBean(origProxy);
		AutoBean<UIComplexInputModelProxy> autoClone = AutoBeanUtils.getAutoBean(cloneProxy);
		AutoBeanCodex.decodeInto(AutoBeanCodex.encode(autoOrig), autoClone);
		
		cloneProxy = context.edit(autoClone.as());
		cloneProxy.getInputs().clear();
		
		for ( AnalyticsOperationInputProxy subInput : origProxy.getInputs() ) {
			AnalyticsOperationInputProxy clone = clone(context,subInput);
			cloneProxy.getInputs().add(clone);
			//cloneProxy.getInputs().add(context.edit(clone));
		}
		
		return cloneProxy;
	}
}