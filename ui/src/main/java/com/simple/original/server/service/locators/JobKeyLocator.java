package com.simple.original.server.service.locators;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.quartz.JobKey;

import com.google.web.bindery.requestfactory.shared.Locator;

public class JobKeyLocator extends Locator<JobKey, String> {

    @Override
    public JobKey create(Class<? extends JobKey> clazz) {
        JobKey key = null;
        
        try {
            Constructor<? extends JobKey> ctor = clazz.getConstructor(String.class);
            key = ctor.newInstance(JobKey.createUniqueName("Analytics"));
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return key;
    }

    @Override
    public JobKey find(Class<? extends JobKey> clazz, String name) {
        String[] values = name.split("@");
        
        return JobKey.jobKey(values[0], values[1]);
    }

    @Override
    public Class<JobKey> getDomainType() {
        return JobKey.class;
    }

    @Override
    public String getId(JobKey domainObject) {
        return domainObject.getName() + "@" + domainObject.getGroup();
    }

    @Override
    public Class<String> getIdType() {
        return String.class;
    }

    @Override
    public Object getVersion(JobKey domainObject) {
        return new Integer(0);
    }

}
