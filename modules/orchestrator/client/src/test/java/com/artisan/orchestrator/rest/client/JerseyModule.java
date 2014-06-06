package com.artisan.orchestrator.rest.client;

import static com.google.inject.Guice.createInjector;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;

import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public abstract class JerseyModule extends GuiceServletContextListener {

	private static final ThreadLocal<Injector> threadLocalInj = new ThreadLocal<Injector>();

	static Injector injector() {
		return threadLocalInj.get();
	}

	private final Logger logger = Logger
			.getLogger(JerseyModule.class.getName());

	protected abstract void configure(Binder binder);

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		super.contextDestroyed(servletContextEvent);
		threadLocalInj.remove();
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		if (threadLocalInj.get() != null) {
			logger.warning("Injector already exists. ServletContextListener.contextDestroyed was not invoked?");
		}
		super.contextInitialized(servletContextEvent);
		Injector in = (Injector) servletContextEvent.getServletContext()
				.getAttribute(Injector.class.getName());
		if (in == null) {
			logger.warning("Injector is not set");
		}
		threadLocalInj.set(in);
	}

	@Override
	protected Injector getInjector() {
		return createInjector(makeJerseyServletModule());
	}

	protected JerseyServletModule makeJerseyServletModule() {
		return new JerseyServletModule() {

			@Override
			protected void configureServlets() {
				JerseyModule.this.configure(binder());
				serve("/*").with(GuiceContainer.class);
			}

		};
	}

}