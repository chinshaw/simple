package com.simple.original.client.proxy;

import java.util.Set;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.Person;
import com.simple.original.api.analytics.IPerson;
import com.simple.original.server.service.locators.PersonLocator;

@ProxyFor(value = Person.class, locator = PersonLocator.class)
public interface PersonProxy extends DatastoreObjectProxy {

	public static final String[] AUTH_PROPERTIES = { "preferences", "userGroup" };

	public Set<IPerson.Role> getRoles();
	
	public String getName();

	public void setName(String name);

	public String getEmail();

	public void setEmail(String email);

	public PreferencesProxy getPreferences();

	public void setPreferences(PreferencesProxy preferences);

	
}
