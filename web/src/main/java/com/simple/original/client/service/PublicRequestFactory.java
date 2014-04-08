package com.simple.original.client.service;

import com.google.web.bindery.requestfactory.shared.ExtraTypes;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.simple.domain.dao.PersonDao;
import com.simple.original.client.proxy.PersonProxy;
import com.simple.original.client.proxy.PreferencesProxy;
import com.simple.original.server.service.AuthenticationService;
import com.simple.original.server.service.InjectingServiceLocator;

public interface PublicRequestFactory extends RequestFactory {

	/**
	 * Service stub for methods in ItemListDao
	 * 
	 * TODO Enhance RequestFactory to enable service stubs to extend a base
	 * interface so we don't have to repeat methods from the base ObjectifyDao
	 * in each stub
	 */

	@ExtraTypes({ PreferencesProxy.class })
	@Service(value = PersonDao.class, locator = InjectingServiceLocator.class)
	interface PublicRequest extends RequestContext {
		/**
		 * @return a request object
		 */
		Request<PersonProxy> findUser(String key);
	}

	@Service(value = AuthenticationService.class, locator = InjectingServiceLocator.class)
	interface AuthenticationRequest extends RequestContext {

		Request<PersonProxy> doAuthenticate(String email, String password);
		
		Request<PersonProxy> getCurrentPerson();

		Request<Void> logout();
	}

	public PublicRequest publicRequest();

	public AuthenticationRequest authRequest();

}
