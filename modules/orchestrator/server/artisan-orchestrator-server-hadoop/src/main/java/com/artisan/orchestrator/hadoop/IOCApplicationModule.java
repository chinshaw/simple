package com.artisan.orchestrator.hadoop;

import com.google.inject.AbstractModule;

public class IOCApplicationModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new IOCOrchestratorModule());
	}

}
