package com.simple.original.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.util.Modules;
import com.simple.engine.IOCEngineModule;
import com.simple.original.security.IOCSecurityModule;
import com.simple.original.server.servlet.IOCServletModule;

public class IOCApplication extends GuiceServletContextListener {

	private static Injector injector;

	@Override
	protected Injector getInjector() {
		injector = Guice.createInjector(getModule());
		return injector;
	}

	public Module getModule() {
		return Modules.combine(new IOCSecurityModule(), new IOCEngineModule(),
				new IOCServletModule());
	}
}