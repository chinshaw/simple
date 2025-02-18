package com.simple.original.server.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.activemq.web.AjaxServlet;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;
import com.simple.original.server.NotificationServlet;
import com.simple.original.server.WebCredentialsLocator;
import com.simple.original.server.service.InjectingServiceLocator;
import com.simple.security.api.ICredentialLocator;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class IOCServletModule extends ServletModule {

	@Override
	protected void configureServlets() {
		
		bind(SecurityManager.class).to(DefaultWebSecurityManager.class).in(
				Scopes.SINGLETON);
		bind(ICredentialLocator.class).to(WebCredentialsLocator.class).in(
				Scopes.SINGLETON);

		bind(ExceptionHandler.class).to(SimpleExceptionHandler.class);
		bind(ServiceLayerDecorator.class).to(
				InjectedServiceLayerDecorator.class);
		bind(InjectingServiceLocator.class);

		//bind(AnalyticsTaskBackupRestoreServlet.class).in(Scopes.SINGLETON);
		bind(NotificationServlet.class).in(Scopes.SINGLETON);

		serve("/rf/public").with(InjectedRequestFactoryServlet.class);
		serve("/rf/secure/dao").with(InjectedRequestFactoryServlet.class);
		serve("/rf/secure/analytics").with(InjectedRequestFactoryServlet.class);
		serve("/rf/secure/analyticsTaskRestore").with(
				InjectedRequestFactoryServlet.class);
		
		serve("/rf/notification").with(NotificationServlet.class);
		
		serve("/rest/v1/*").with(GuiceContainer.class, createJerseyParams());
		
		bind(AjaxServlet.class).in(Scopes.SINGLETON);
		serve("/amq/*").with(AjaxServlet.class);
		
		filter("/*").through(ShiroFilter.class);
	}

	@SuppressWarnings("serial")
	protected HashMap<String, String> createAtmosphereParams() {

		return new HashMap<String, String>() {

			{
				put("org.atmosphere.disableOnStateEvent", "true");
				put("org.atmosphere.cpr.sessionSupport", "true");
				put("org.atmosphere.cpr.CometSupport.maxInactiveActivity",
						"300000");
				put("org.atmosphere.cpr.broadcasterLifeCyclePolicy",
						"EMPTY_DESTROY");
				put("org.atmosphere.cpr.recoverFromDestroyedBroadcaster",
						"true");
			}
		};
	}

	@SuppressWarnings("serial")
	protected HashMap<String, String> createActiveMqParams() {
		return new HashMap<String, String>() {
			{
				put("org.apache.activemq.brokerURL", "vm://localhost");
			}
		};
	}
	
	

	/**
	 * Creates and reuses injecting JSR 303 Validator factory.
	 * 
	 * @param injector
	 *            the injector that will be used for the injection.
	 * @return The ValidatorFactory.
	 */
	@Provides
	@Singleton
	public ValidatorFactory getValidatorFactory(Injector injector) {
		return Validation
				.byDefaultProvider()
				.configure()
				.constraintValidatorFactory(
						new InjectingConstraintValidationFactory(injector))
				.buildValidatorFactory();
	}

	/**
	 * Creates and reuses injecting JSR 303 Validator.
	 * 
	 * @param validatorFactory
	 *            the ValidatorFactory to get the Validator from.
	 * @return the Validator.
	 */
	@Provides
	@Singleton
	public Validator getValidator(ValidatorFactory validatorFactory) {
		return validatorFactory.getValidator();
	}
	

	private Map<String, String> createJerseyParams() {
		Map<String, String> params = new HashMap<String, String>();

		// params.put(
		// "com.sun.jersey.server.impl.cdi.lookupExtensionInBeanManager",
		// "true");
		params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
		params.put("com.sun.jersey.spi.container.ResourceFilters",
				"com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFacto‌​ry");

		return params;
	}

}
