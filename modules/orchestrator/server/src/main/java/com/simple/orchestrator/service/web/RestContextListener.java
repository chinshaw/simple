package com.simple.orchestrator.service.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.simple.orchestrator.IOCOrchestratorWebModule;

public class RestContextListener extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new IOCOrchestratorWebModule());
	}
}
