package com.simple.orchestrator.service.web;

import javax.servlet.annotation.WebListener;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.simple.orchestrator.IOCApplicationInjectorFactory;

@WebListener("Orchestrator IOC Injection Listener")
public class WebContextInjectorListener extends GuiceServletContextListener {
	
	@Override
	protected Injector getInjector() {
		return IOCApplicationInjectorFactory.getInjector();
	}
}
