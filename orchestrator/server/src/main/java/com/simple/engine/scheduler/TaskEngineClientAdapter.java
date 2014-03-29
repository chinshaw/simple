package com.simple.engine.scheduler;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class TaskEngineClientAdapter {
    private static TaskEngineClientAdapter instance = null;
    private static Scheduler sched = null;

    /**
     * Retrieve singleton instance.
     * 
     * @return
     * @throws SchedulerException
     */
    public static final TaskEngineClientAdapter getInstance() throws SchedulerException {
        if (instance == null) {
            instance = new TaskEngineClientAdapter();
        }
        return instance;
    }

    /**
     * Private constructor this will instantiate the task engine client that we
     * can use to schedule jobs.
     * 
     * @throws SchedulerException
     */
    private TaskEngineClientAdapter() throws SchedulerException {
        SchedulerFactory sf = new StdSchedulerFactory();
        sched = sf.getScheduler();
    }

    /**
     * Add the given <code>{@link org.quartz.JobDetail}</code> to the Scheduler,
     * and associate the given <code>{@link Trigger}</code> with it.
     * 
     * <p>
     * If the given Trigger does not reference any <code>Job</code>, then it
     * will be set to reference the Job passed with it into this method.
     * </p>
     * 
     * @throws SchedulerException
     *             if the Job or Trigger cannot be added to the Scheduler, or
     *             there is an internal Scheduler error.
     */
    Date scheduleReport(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        return sched.scheduleJob(jobDetail, trigger);
    }
}
