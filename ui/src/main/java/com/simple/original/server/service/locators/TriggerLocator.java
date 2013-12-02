package com.simple.original.server.service.locators;

import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Locator;
import com.simple.engine.scheduler.TaskEngineClient;

public class TriggerLocator extends Locator<Trigger, TriggerKey> {

	private TaskEngineClient client;
	
	@Inject
	private TriggerLocator(TaskEngineClient client) {
		this.client = client;
	}
	
    @Override
    public Trigger create(Class<? extends Trigger> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Trigger find(Class<? extends Trigger> clazz, TriggerKey triggerKey) {
        Trigger trigger = null;
        try {
            trigger = client.getTrigger(triggerKey);
        } catch (SchedulerException e) {
            throw new RuntimeException("Error while fetching Key " + e);
        } 
        return trigger;
    }

    @Override
    public Class<Trigger> getDomainType() {
        return Trigger.class;
    }

    @Override
    public TriggerKey getId(Trigger domainObject) {
        return domainObject.getKey();
    }

    @Override
    public Class<TriggerKey> getIdType() {
        return TriggerKey.class;
    }

    @Override
    public Object getVersion(Trigger domainObject) {
        return new Integer(0);
    }
}
