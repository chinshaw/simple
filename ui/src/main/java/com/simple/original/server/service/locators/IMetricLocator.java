package com.simple.original.server.service.locators;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Locator;
import com.simple.original.api.orchestrator.IMetric;
import com.simple.original.api.orchestrator.IMetricKey;

public class IMetricLocator extends Locator<IMetric, IMetricKey> {

	private final EntityManager em;
	
	@Inject
	public IMetricLocator(EntityManager em) {
		this.em = em;
	}
	
    @Override
    public IMetric create(Class<? extends IMetric> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IMetric find(Class<? extends IMetric> clazz, IMetricKey id) {
        return em.find(clazz, id);
    }

    @Override
    public Class<IMetric> getDomainType() {
        return IMetric.class;
    }

    @Override
    public IMetricKey getId(IMetric domainObject) {
        return domainObject.getKey();
    }

    @Override
    public Class<IMetricKey> getIdType() {
        return IMetricKey.class;
    }

    /**
     * The version for an IMetricEntity will never be different, Metrics are 
     * created but never updated so this can always return 0;
     */
    @Override
    public Integer getVersion(IMetric domainObject) {
        return 0;
    }
}
