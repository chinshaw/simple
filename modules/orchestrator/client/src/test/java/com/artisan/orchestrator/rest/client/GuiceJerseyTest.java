package com.artisan.orchestrator.rest.client;

import org.junit.Before;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

public abstract class GuiceJerseyTest extends JerseyTest {

	public GuiceJerseyTest(Class<? extends JerseyModule> guiceConfClass) {
		super(new WebAppDescriptor.Builder().filterClass(GuiceFilter.class)
				.contextListenerClass(guiceConfClass).build());
	}

	protected Injector injector;

	@Before
	public void inject() {
		injector = JerseyModule.injector();
		injector.injectMembers(this);
	}
}
