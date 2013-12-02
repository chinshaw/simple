package com.simple.original.client.proxy;

import org.quartz.JobKey;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.original.server.service.locators.JobKeyLocator;

@ProxyFor(value = JobKey.class, locator=JobKeyLocator.class)
public interface QuartzJobKeyProxy extends EntityProxy {
    /**
     * <p>
     * Get the name portion of the key.
     * </p>
     * 
     * @return the name
     */
    public String getName();

    /**
     * <p>
     * Get the group portion of the key.
     * </p>
     * 
     * @return the group
     */
    public String getGroup();
}
