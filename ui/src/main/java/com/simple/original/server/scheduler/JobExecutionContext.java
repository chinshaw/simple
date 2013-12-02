package com.simple.original.server.scheduler;

import java.util.Date;

import org.quartz.Calendar;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

/**
 * This class is to support the ui in order to view running tasks.
 * The JobExecutionContextImpl does not have a no arg constructor so this is necessary.
 * 
 * @author chinshaw
 *
 */
public class JobExecutionContext implements org.quartz.JobExecutionContext {

    private  org.quartz.JobExecutionContext jobContext;
    
    
    /**
     * Should only be used by the locator service.
     */
    public JobExecutionContext() {
        
    }
    
    public JobExecutionContext( org.quartz.JobExecutionContext jobContext) {
        this.jobContext = jobContext;
    }
    
    @Override
    public Scheduler getScheduler() {
        return jobContext.getScheduler();
    }

    @Override
    public Trigger getTrigger() {
        return jobContext.getTrigger();
    }

    @Override
    public Calendar getCalendar() {
        return jobContext.getCalendar();
    }

    @Override
    public boolean isRecovering() {
        return jobContext.isRecovering();
    }

    @Override
    public int getRefireCount() {
        return jobContext.getRefireCount();
    }

    @Override
    public JobDataMap getMergedJobDataMap() {
        return jobContext.getMergedJobDataMap();
    }

    @Override
    public JobDetail getJobDetail() {
        return jobContext.getJobDetail();
    }

    @Override
    public Job getJobInstance() {
        return jobContext.getJobInstance();
    }

    @Override
    public Date getFireTime() {
        return jobContext.getFireTime();
    }

    @Override
    public Date getScheduledFireTime() {
        return jobContext.getScheduledFireTime();
    }

    @Override
    public Date getPreviousFireTime() {
        return jobContext.getPreviousFireTime();
    }

    @Override
    public Date getNextFireTime() {
        return jobContext.getNextFireTime();
    }

    @Override
    public String getFireInstanceId() {
        return jobContext.getFireInstanceId();
    }

    @Override
    public Object getResult() {
         return jobContext.getResult();
    }

    @Override
    public void setResult(Object result) {
        jobContext.setResult(result);
    }

    @Override
    public long getJobRunTime() {
        return jobContext.getJobRunTime();
    }

    @Override
    public void put(Object key, Object value) {
        jobContext.put(key, value);
        
    }

    @Override
    public Object get(Object key) {
        return jobContext.get(key);
    }

	@Override
	public TriggerKey getRecoveringTriggerKey() throws IllegalStateException {
		return jobContext.getRecoveringTriggerKey();
	}
}
