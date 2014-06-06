package com.simple.orchestrator;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;


@ApplicationPath("rest/v1")
public class IOCRestApplication extends ResourceConfig {
	
	@Inject
	public IOCRestApplication(ServiceLocator serviceLocator) {
		packages("com.simple.orchestrator.service.web.rest");
		
		GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
		GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
		guiceBridge.bridgeGuiceInjector(IOCApplicationInjectorFactory.getInjector());
	}
}