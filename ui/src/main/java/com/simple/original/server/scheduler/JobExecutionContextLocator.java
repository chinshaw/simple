package com.simple.original.server.scheduler;

import org.quartz.JobExecutionContext;

import com.google.web.bindery.requestfactory.shared.Locator;

public class JobExecutionContextLocator extends Locator<JobExecutionContext, String> {

    @Override
    public JobExecutionContext create(Class<? extends JobExecutionContext> clazz) {
        JobExecutionContext jobContext = null;
        
        try {
            return clazz.newInstance(); 
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return jobContext;
    }

    @Override
    public JobExecutionContext find(Class<? extends JobExecutionContext> clazz, String id) {
        return null;
    }

    @Override
    public Class<JobExecutionContext> getDomainType() {
        return JobExecutionContext.class;
    }

    @Override
    public String getId(JobExecutionContext domainObject) {
        return "NO ID";
    }

    @Override
    public Class<String> getIdType() {
        return String.class;
    }

    @Override
    public Object getVersion(JobExecutionContext domainObject) {
        return new Integer(0);
    }
}
