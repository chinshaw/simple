package com.simple.orchestrator;

import com.google.inject.AbstractModule;
import com.simple.orchestrator.server.IOCApplicationModule;

public class IOCOrchestratorTestModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new IOCApplicationModule());
	}
}
