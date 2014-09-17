package com.simple.orchestrator.server.service.web;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.simple.orchestrator.server.IOCApplicationInjector;

public class WebContextInjectorListener extends GuiceServletContextListener {
	
	@Override
	protected Injector getInjector() {
		return IOCApplicationInjector.getInjector();
	}
}
