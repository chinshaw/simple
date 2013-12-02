package com.simple.original.server.service.locators;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.quartz.TriggerKey;

import com.google.web.bindery.requestfactory.shared.Locator;

public class TriggerKeyLocator extends Locator<TriggerKey, String> {

    @Override
    public TriggerKey create(Class<? extends TriggerKey> clazz) {
        TriggerKey key = null;
        
        try {
            Constructor<? extends TriggerKey> ctor = clazz.getConstructor(String.class);
            key = ctor.newInstance(TriggerKey.createUniqueName("Analytics"));
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
    public TriggerKey find(Class<? extends TriggerKey> clazz, String id) {
        return TriggerKey.triggerKey(id, "Analytics");
    }

    @Override
    public Class<TriggerKey> getDomainType() {
        return TriggerKey.class;
    }

    @Override
    public String getId(TriggerKey domainObject) {
        return domainObject.getName();
    }

    @Override
    public Class<String> getIdType() {
        return String.class;
    }

    @Override
    public Object getVersion(TriggerKey domainObject) {
        return new Integer(0);
    }

}
