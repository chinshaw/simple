package com.simple.orchestrator;

import com.google.inject.Injector;
import com.simple.orchestrator.server.IOCApplicationInjector;

public class OrchestratorTest  {

	
	public OrchestratorTest() {

	}
	
	public Injector getInjector() {
		return IOCApplicationInjector.getInjector();
	}
}
