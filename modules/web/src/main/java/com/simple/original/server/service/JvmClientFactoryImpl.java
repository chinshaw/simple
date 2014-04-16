package com.simple.original.server.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.web.bindery.requestfactory.vm.RequestFactorySource;
import com.google.web.bindery.requestfactory.vm.testing.UrlRequestTransport;
import com.simple.original.client.service.DaoRequestFactory;
import com.simple.original.client.service.PublicRequestFactory;
import com.simple.original.client.service.ServiceRequestFactory;

/**
 * This class provides implementation to instantiate the GWT client side request
 * factory interfaces on a JVM. This class can be used to call the methods in
 * request factories from server / standalone clients
 * 
 * @author valva
 * 
 */
public class JvmClientFactoryImpl {

	private static final Logger logger = Logger.getLogger(JvmClientFactoryImpl.class.getName());
	/**
	 * Instances of DaoRequestFactory
	 */
	private DaoRequestFactory daoRequestFactory;

	/**
	 * Instances of PublicRequestFactory
	 */
	private PublicRequestFactory publicRequestFactory;

	/**
	 * Instances of ServiceRequestFactory
	 */
	private ServiceRequestFactory serviceRequestFactory;

	@Inject
	@Named("com.simple.app.base.url")
	private static String BASE_URL;
	private static String RemoteRequestFactory_URL = BASE_URL + "/remoteRequestFactory";
	private static String DaoRequestFactory_URL = BASE_URL + "/dao";
	private static String PublicRequestFactory_URL = BASE_URL + "/public";
	private static String ServiceRequestFactory_URL = BASE_URL + "/analytics";

	/**
	 * Private constructor which initializes all the request factories
	 */

	private JvmClientFactoryImpl(String appUrl) {

		EventBus eventBus = new SimpleEventBus();
		URL urlDaoReqUrl = null;
		URL urlPublicReqUrl = null;
		URL urlServiceReqUrl = null;

		/* Initialize RemoteRequestFactory */
		try {

			urlDaoReqUrl = new URL(DaoRequestFactory_URL);
			urlPublicReqUrl = new URL(PublicRequestFactory_URL);
			urlServiceReqUrl = new URL(ServiceRequestFactory_URL);
		} catch (MalformedURLException e) {
			logger.severe("JvmClientFactoryImpl(): Unable to form URL - " + e.getMessage());
		}

		daoRequestFactory = RequestFactorySource.create(DaoRequestFactory.class);
		UrlRequestTransport daoRequestTransport = new UrlRequestTransport(urlDaoReqUrl);
		daoRequestFactory.initialize(eventBus, daoRequestTransport);

		publicRequestFactory = RequestFactorySource.create(PublicRequestFactory.class);
		UrlRequestTransport publicRequestTransport = new UrlRequestTransport(urlPublicReqUrl);
		publicRequestFactory.initialize(eventBus, publicRequestTransport);

		serviceRequestFactory = RequestFactorySource.create(ServiceRequestFactory.class);
		UrlRequestTransport serviceRequestTransport = new UrlRequestTransport(urlServiceReqUrl);
		serviceRequestFactory.initialize(eventBus, serviceRequestTransport);
	}

	/**
	 * Instance of DaoRequestFactory
	 * 
	 * @return daoRequestFactory DaoRequestFactory
	 */
	public DaoRequestFactory daoRequestFactory() {
		return this.daoRequestFactory;
	}

	/**
	 * Instance of PublicRequestFactory
	 * 
	 * @return publicRequestFactory PublicRequestFactory
	 */
	public PublicRequestFactory publicRequestFactory() {
		return publicRequestFactory;
	}

	/**
	 * Instance of ServiceRequestFactory
	 * 
	 * @return serviceRequestFactory ServiceRequestFactory
	 */
	public ServiceRequestFactory servicRequestFactory() {
		return serviceRequestFactory;
	}
}
