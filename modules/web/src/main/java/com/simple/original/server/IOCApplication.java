package com.simple.original.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.util.Modules;
import com.simple.domain.IOCDomainModule;
import com.simple.original.server.service.rest.IOCRestModule;
import com.simple.original.server.servlet.IOCServletModule;
import com.simple.security.IOCSecurityModule;

public class IOCApplication extends GuiceServletContextListener {

	private static Injector injector = Guice.createInjector(getModule());

	@Override
	protected Injector getInjector() {
		return injector;
	}

	public static Module getModule() {
		return Modules.combine(new IOCDomainModule(), new IOCSecurityModule(), 
				new IOCServletModule(), new IOCRestModule());
		
	}
}