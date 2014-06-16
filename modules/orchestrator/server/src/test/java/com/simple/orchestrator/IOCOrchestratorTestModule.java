package com.simple.orchestrator;

import com.google.inject.AbstractModule;

public class IOCOrchestratorTestModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new IOCApplicationModule());
	}
}
