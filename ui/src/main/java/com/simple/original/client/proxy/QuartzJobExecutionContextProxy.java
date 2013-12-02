package com.simple.original.client.proxy;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.simple.original.server.scheduler.JobExecutionContext;


/**
 * This class has a locator becuase not all the
 * @author chinshaw
 *
 */
@ProxyFor(value = JobExecutionContext.class)
public interface QuartzJobExecutionContextProxy extends ValueProxy {

    public static String PROPERTIES[] = { "trigger", "trigger.jobKey" };
    
    /**
     * <p>
     * Get a handle to the <code>Trigger</code> instance that fired the
     * <code>Job</code>.
     * </p>
     */
    public QuartzTriggerProxy getTrigger();

    /**
     * <p>
     * If the <code>Job</code> is being re-executed because of a 'recovery'
     * situation, this method will return <code>true</code>.
     * </p>
     */
    public boolean isRecovering();

    public int getRefireCount();

    /**
     * <p>
     * Get the instance of the <code>Job</code> that was created for this
     * execution.
     * </p>
     * 
     * <p>
     * Note: The Job instance is not available through remote scheduler
     * interfaces.
     * </p>
     */
    //public Job getJobInstance();

    /**
     * The actual time the trigger fired. For instance the scheduled time may
     * have been 10:00:00 but the actual fire time may have been 10:00:03 if
     * the scheduler was too busy.
     * 
     * @return Returns the fireTime.
     * @see #getScheduledFireTime()
     */
    public Date getFireTime();

    /**
     * The scheduled time the trigger fired for. For instance the scheduled
     * time may have been 10:00:00 but the actual fire time may have been
     * 10:00:03 if the scheduler was too busy.
     * 
     * @return Returns the scheduledFireTime.
     * @see #getFireTime()
     */
    public Date getScheduledFireTime();

    public Date getPreviousFireTime();

    public Date getNextFireTime();

    /**
     * Get the unique Id that identifies this particular firing instance of the
     * trigger that triggered this job execution.  It is unique to this 
     * JobExecutionContext instance as well.
     * 
     * @return the unique fire instance id
     */
    public String getFireInstanceId();
    
    /**
     * The amount of time the job ran for (in milliseconds).  The returned 
     * value will be -1 until the job has actually completed (or thrown an 
     * exception), and is therefore generally only useful to 
     * <code>JobListener</code>s and <code>TriggerListener</code>s.
     * 
     * @return Returns the jobRunTime.
     */
    public long getJobRunTime();    
}
