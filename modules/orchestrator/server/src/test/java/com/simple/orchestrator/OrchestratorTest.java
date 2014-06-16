package com.simple.orchestrator;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class OrchestratorTest  {

	
	private Injector injector;
	
	public OrchestratorTest() {
		injector = Guice.createInjector(new IOCOrchestratorTestModule());
	}
	
	public Injector getInjector() {
		return injector;
	}
}
