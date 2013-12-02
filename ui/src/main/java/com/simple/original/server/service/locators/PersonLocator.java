package com.simple.original.server.service.locators;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Locator;
import com.simple.domain.Person;

/**
 * This class is the locator for all things that reside in the datastore.
 * It handles looking up objects by their id for RequestFactory. See the 
 * @see Locator
 * @author chinshaw
 */
public class PersonLocator extends Locator<Person, Long> {

	private final EntityManager em;
	
	@Inject
	public PersonLocator(EntityManager em) {
		this.em = em;
	}
	
    @Override
    public Person create(Class<? extends Person> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Person find(Class<? extends Person> clazz, Long id) {
        return em.find(clazz,id);
    }

    @Override
    public Class<Person> getDomainType() {
        return Person.class;
    }

    @Override
    public Long getId(Person domainObject) {
        return domainObject.getId();
    }

    @Override
    public Class<Long> getIdType() {
        return Long.class;
    }

    @Override
    public Integer getVersion(Person domainObject) {
        return domainObject.getVersion();
    }
}
