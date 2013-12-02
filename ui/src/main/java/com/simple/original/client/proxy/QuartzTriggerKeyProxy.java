package com.simple.original.client.proxy;

import org.quartz.TriggerKey;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.original.server.service.locators.TriggerKeyLocator;


@ProxyFor(value = TriggerKey.class, locator=TriggerKeyLocator.class)
public interface QuartzTriggerKeyProxy extends EntityProxy {
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
