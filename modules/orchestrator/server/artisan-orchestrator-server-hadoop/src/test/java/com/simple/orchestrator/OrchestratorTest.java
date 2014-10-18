package com.simple.orchestrator;

import com.artisan.orchestrator.hadoop.IOCApplicationInjector;
import com.google.inject.Injector;

public class OrchestratorTest  {

	
	public OrchestratorTest() {

	}
	
	public Injector getInjector() {
		return IOCApplicationInjector.getInjector();
	}
}
