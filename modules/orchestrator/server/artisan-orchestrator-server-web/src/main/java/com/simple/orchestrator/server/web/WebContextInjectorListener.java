package com.simple.orchestrator.server.web;

import com.artisan.orchestrator.hadoop.IOCApplicationInjector;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class WebContextInjectorListener extends GuiceServletContextListener {
	
	@Override
	protected Injector getInjector() {
		return IOCApplicationInjector.getInjector();
	}
}
