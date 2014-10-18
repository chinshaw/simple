package com.simple.orchestrator.service.web.rest;

import com.google.inject.Provides;
import com.google.inject.servlet.ServletModule;
import com.simple.api.orchestrator.IPerson;
import com.simple.orchestrator.IOCOrchestratorTestModule;

public class IOCTestOrchestratorRestModule extends ServletModule {

	@Override
	public void configureServlets() {
		install(new IOCOrchestratorTestModule());
	}

	/*
	@Provides
	ISession sessionProvider() {
		IPerson fakePerson = null;
		fakePerson = new Person("Fake Person", "fake@tester.com");

		FakeSession session = new FakeSession();
		session.setCurrentPerson(fakePerson);
		return session;
	}
	*/
}