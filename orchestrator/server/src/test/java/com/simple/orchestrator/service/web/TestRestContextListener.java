package com.simple.orchestrator.service.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.util.Modules;
import com.simple.orchestrator.IOCOrchestratorRestModule;
import com.simple.orchestrator.service.web.rest.IOCTestOrchestratorRestModule;

public class TestRestContextListener extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(Modules.override(new IOCOrchestratorRestModule()).with(new IOCTestOrchestratorRestModule()));
	}
}
