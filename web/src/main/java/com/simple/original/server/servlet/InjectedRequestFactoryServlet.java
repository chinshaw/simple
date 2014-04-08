package com.simple.original.server.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;

@Singleton
public class InjectedRequestFactoryServlet extends RequestFactoryServlet {

	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = 8872402359510571614L;


	@Inject
	protected InjectedRequestFactoryServlet(final ExceptionHandler exceptionHandler, final ServiceLayerDecorator serviceLayerDecorator) {
		super(exceptionHandler, serviceLayerDecorator);
	}
}