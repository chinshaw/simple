package com.simple.original.server.service.locators;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Locator;
import com.simple.api.orchestrator.IRequestFactoryEntity;

/**
 * This class is the locator for all things that reside in the datastore. It
 * handles looking up objects by their id for RequestFactory. See the
 * 
 * @see Locator
 * @author chinshaw
 */
public class RequestFactoryEntityLocator extends
		Locator<IRequestFactoryEntity, Long> {

	private EntityManager em;

	@Inject
	public RequestFactoryEntityLocator(EntityManager em) {
		this.em = em;
	}

	@Override
	public IRequestFactoryEntity create(
			Class<? extends IRequestFactoryEntity> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public IRequestFactoryEntity find(
			Class<? extends IRequestFactoryEntity> clazz, Long id) {
		return em.find(clazz, id);
	}

	@Override
	public Class<IRequestFactoryEntity> getDomainType() {
		return IRequestFactoryEntity.class;
	}

	@Override
	public Long getId(IRequestFactoryEntity domainObject) {
		return domainObject.getId();
	}

	@Override
	public Class<Long> getIdType() {
		return Long.class;
	}

	@Override
	public Integer getVersion(IRequestFactoryEntity domainObject) {
		return domainObject.getVersion();
	}
}
