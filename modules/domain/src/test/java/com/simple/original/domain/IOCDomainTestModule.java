package com.simple.original.domain;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.simple.domain.PersistenceInitializer;
import com.simple.original.domain.dao.FakeSession;
import com.simple.security.api.ISession;

public class IOCDomainTestModule extends AbstractModule {

	
	@Override
	protected void configure() {
		install(new JpaPersistModule("simple-pu"));
		bind(PersistenceInitializer.class).asEagerSingleton();
		bind(ISession.class).to(FakeSession.class);
	}
}
