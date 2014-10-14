package com.simple.orchestrator.server;

import com.google.inject.Guice;
import com.google.inject.Injector;


/**
 * Simple singleton class to fecth the injector. This can be used within the context
 * of the same container so for instance a unit test can use this and the
 * WebContextInjectorListener can also fetch the same class. This is the preferred
 * wey to get the ApplicationInjector, not the dependencies can be overrridden
 * programmatically at a later time.
 * 
 * @author chris
 *
 */
public class IOCApplicationInjector {

	private static Injector injector = Guice.createInjector(new IOCApplicationModule());
	
	public static final Injector getInjector() {
		return injector;
	}
}
