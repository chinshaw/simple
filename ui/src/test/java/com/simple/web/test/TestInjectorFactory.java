package com.simple.web.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class TestInjectorFactory {

	private static Injector injector = null;

	public static final Injector getInjector() {
		if (injector == null) {
			injector = Guice.createInjector(getModules());
		}
		return injector;
	}

	private static Module getModules() {
		return new IOCTestWebModule();
	}

}
