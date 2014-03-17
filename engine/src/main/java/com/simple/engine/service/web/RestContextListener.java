package com.simple.engine.service.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.simple.engine.IOCEngineRestModule;

public class RestContextListener extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new IOCEngineRestModule());
	}
}
