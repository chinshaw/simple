package com.simple.original.client.proxy;

import org.quartz.impl.triggers.CronTriggerImpl;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.original.server.service.locators.TriggerLocator;

@ProxyFor(value = CronTriggerImpl.class, locator = TriggerLocator.class)
public interface QuartzCronTriggerProxy extends QuartzTriggerProxy {

    public void setCronExpression(String cronExpression);

    public String getCronExpression();

    public String getExpressionSummary();

    public void setName(String name);

    public String getName();

}
