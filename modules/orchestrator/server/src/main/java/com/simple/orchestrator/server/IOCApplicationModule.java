package com.simple.orchestrator.server;

import com.google.inject.AbstractModule;

public class IOCApplicationModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new IOCOrchestratorModule());
		install(new IOCOrchestratorWebModule());
	}

}
