package com.simple.web.test;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.simple.engine.IOCEngineModule;
import com.simple.original.api.security.ISession;
import com.simple.original.server.service.DashboardService;

public class IOCTestWebModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new IOCEngineModule());
		bind(DashboardService.class).in(Scopes.SINGLETON);
		bind(ISession.class).to(FakeSession.class);
	}	
}
