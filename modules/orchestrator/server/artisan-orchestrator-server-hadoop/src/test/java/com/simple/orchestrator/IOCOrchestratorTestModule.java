package com.simple.orchestrator;

import com.artisan.orchestrator.hadoop.IOCApplicationModule;
import com.google.inject.AbstractModule;

public class IOCOrchestratorTestModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new IOCApplicationModule());
	}
}
