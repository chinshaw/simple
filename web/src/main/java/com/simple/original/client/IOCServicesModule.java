package com.simple.original.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.gwt.client.DefaultRequestTransport;
import com.simple.original.client.service.DaoRequestFactory;
import com.simple.original.client.service.PublicRequestFactory;
import com.simple.original.client.service.ServiceRequestFactory;

public class IOCServicesModule extends AbstractGinModule {

	@Override
	protected void configure() {
	}

	@Inject
	@Provides
	@Singleton
	DaoRequestFactory getDaoRequestFactory(EventBus eventBus) {
		DaoRequestFactory factory = GWT.create(DaoRequestFactory.class);
		DefaultRequestTransport transport = new DefaultRequestTransport();
		transport.setRequestUrl(GWT.getHostPageBaseURL() + "rf/secure/dao");
		factory.initialize(eventBus, transport);
		return factory;
	}

	@Inject
	@Provides
	@Singleton
	PublicRequestFactory getPublicRequestFactory(EventBus eventBus) {
		final PublicRequestFactory factory = GWT
				.create(PublicRequestFactory.class);
		DefaultRequestTransport transport = new DefaultRequestTransport();
		transport.setRequestUrl(GWT.getHostPageBaseURL() + "rf/public");
		factory.initialize(eventBus, transport);
		return factory;
	}

	@Inject
	@Provides
	@Singleton
	ServiceRequestFactory getServiceRequestFactory(EventBus eventBus) {
		final ServiceRequestFactory factory = GWT
				.create(ServiceRequestFactory.class);
		DefaultRequestTransport transport = new DefaultRequestTransport();
		transport.setRequestUrl(GWT.getHostPageBaseURL() + "rf/secure/analytics");
		factory.initialize(eventBus, transport);
		return factory;
	}
}
