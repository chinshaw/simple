package com.simple.original.client.proxy;

import java.util.Date;

import org.quartz.Calendar;
import org.quartz.CronTrigger;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.original.server.service.locators.TriggerLocator;


/**
 * This class has a locator becuase not all the
 * @author chinshaw
 *
 */
@ProxyFor(value=Trigger.class, locator=TriggerLocator.class)
public interface QuartzTriggerProxy extends EntityProxy {

    public static String[] PROPERTIES = { "key", "jobKey" };
    
    /**
     * Getter for the job keys.
     * @return
     */
    public QuartzJobKeyProxy getJobKey();
    
    /**
     * Getter for the trigger key.
     * @return
     */
    public QuartzTriggerKeyProxy getKey();
    
    /**
     * Return the description given to the <code>Trigger</code> instance by
     * its creator (if any).
     * 
     * @return null if no description was set.
     */
    public String getDescription();

    /**
     * Get the name of the <code>{@link Calendar}</code> associated with this
     * Trigger.
     * 
     * @return <code>null</code> if there is no associated Calendar.
     */
    public String getCalendarName();


    /**
     * The priority of a <code>Trigger</code> acts as a tiebreaker such that if 
     * two <code>Trigger</code>s have the same scheduled fire time, then the
     * one with the higher priority will get first access to a worker
     * thread.
     * 
     * <p>
     * If not explicitly set, the default value is <code>5</code>.
     * </p>
     * 
     * @see #DEFAULT_PRIORITY
     */
    public int getPriority();


    /**
     * Get the time at which the <code>Trigger</code> should occur.
     */
    public Date getStartTime();

    /**
     * Get the time at which the <code>Trigger</code> should quit repeating -
     * regardless of any remaining repeats (based on the trigger's particular 
     * repeat settings). 
     * 
     * @see #getFinalFireTime()
     */
    public Date getEndTime();

    /**
     * Returns the next time at which the <code>Trigger</code> is scheduled to fire. If
     * the trigger will not fire again, <code>null</code> will be returned.  Note that
     * the time returned can possibly be in the past, if the time that was computed
     * for the trigger to next fire has already arrived, but the scheduler has not yet
     * been able to fire the trigger (which would likely be due to lack of resources
     * e.g. threads).
     *
     * <p>The value returned is not guaranteed to be valid until after the <code>Trigger</code>
     * has been added to the scheduler.
     * </p>
     *
     * @see TriggerUtils#computeFireTimesBetween(Trigger, Calendar, Date, Date)
     */
    public Date getNextFireTime();

    /**
     * Returns the previous time at which the <code>Trigger</code> fired.
     * If the trigger has not yet fired, <code>null</code> will be returned.
     */
    public Date getPreviousFireTime();

    /**
     * Returns the last time at which the <code>Trigger</code> will fire, if
     * the Trigger will repeat indefinitely, null will be returned.
     * 
     * <p>
     * Note that the return time *may* be in the past.
     * </p>
     */
    public Date getFinalFireTime();

    /**
     * Get the instruction the <code>Scheduler</code> should be given for
     * handling misfire situations for this <code>Trigger</code>- the
     * concrete <code>Trigger</code> type that you are using will have
     * defined a set of additional <code>MISFIRE_INSTRUCTION_XXX</code>
     * constants that may be set as this property's value.
     * 
     * <p>
     * If not explicitly set, the default value is <code>MISFIRE_INSTRUCTION_SMART_POLICY</code>.
     * </p>
     * 
     * @see #MISFIRE_INSTRUCTION_SMART_POLICY
     * @see #updateAfterMisfire(Calendar)
     * @see SimpleTrigger
     * @see CronTrigger
     */
    public int getMisfireInstruction();

}
