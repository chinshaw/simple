package com.artisan.orchestrator.hadoop.scheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import org.quartz.Calendar;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.ListenerManager;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.UnableToInterruptJobException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.JobFactory;

import com.google.inject.Inject;

public class TaskEngineClient implements Scheduler {

    /**
     * Logger
     */
    private static final Logger logger = Logger.getLogger(TaskEngineClient.class.getName());

    /**
     * Quartz Scheduler object.
     */
    private Scheduler scheduler = null;


    
    
    /**
     * Getter for the singleton instance.
     * 
     * @throws SchedulerException
     */
    @Inject
    public TaskEngineClient(Properties schedulerProperties) throws SchedulerException {

        logger.info("Securiting manager is " + System.getSecurityManager());
        
        if (System.getSecurityManager() == null) {
            System.out.println("Security manager is null");
        }

        SchedulerFactory sf = new StdSchedulerFactory(schedulerProperties);
        scheduler = sf.getScheduler();
    }

    public List<Trigger> listScheduledJobs() throws SchedulerException {
        List<Trigger> jobs = new ArrayList<Trigger>();

        List<String> triggerGroups = getTriggerGroupNames();
        logger.info("Number of trigger groups is " + triggerGroups.size());

        for (String triggerGroup : triggerGroups) {
            Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(triggerGroup));
            for (TriggerKey triggerKey : triggerKeys) {
                Trigger trigger = scheduler.getTrigger(triggerKey);
                jobs.add(trigger);
                // JobKey jobKey = trigger.getJobKey();
            }
        }
        return jobs;
    }

    /**
     * Fetches the scheduled jobs based on their group names.
     */
    public List<Trigger> listScheduledJobs(String groupName) throws SchedulerException {
        List<Trigger> jobs = new ArrayList<Trigger>();
        Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(groupName));
        for (TriggerKey triggerKey : triggerKeys) {
            Trigger trigger = scheduler.getTrigger(triggerKey);
            jobs.add(trigger);
        }
        return jobs;
    }

    @Override
    public String getSchedulerName() throws SchedulerException {
        return scheduler.getSchedulerName();
    }

    @Override
    public String getSchedulerInstanceId() throws SchedulerException {
        return scheduler.getSchedulerInstanceId();
    }

    @Override
    public SchedulerContext getContext() throws SchedulerException {
        return scheduler.getContext();
    }

    @Override
    public void start() throws SchedulerException {
        scheduler.start();
    }

    @Override
    public void startDelayed(int seconds) throws SchedulerException {
        scheduler.startDelayed(seconds);
    }

    @Override
    public boolean isStarted() throws SchedulerException {
        return scheduler.isStarted();
    }

    @Override
    public void standby() throws SchedulerException {
        scheduler.standby();

    }

    @Override
    public boolean isInStandbyMode() throws SchedulerException {
        return scheduler.isInStandbyMode();
    }

    @Override
    public void shutdown() throws SchedulerException {
        scheduler.shutdown();

    }

    @Override
    public void shutdown(boolean waitForJobsToComplete) throws SchedulerException {
        scheduler.shutdown(waitForJobsToComplete);

    }

    @Override
    public boolean isShutdown() throws SchedulerException {
        return scheduler.isShutdown();
    }

    @Override
    public SchedulerMetaData getMetaData() throws SchedulerException {
        return scheduler.getMetaData();
    }

    @Override
    public List<JobExecutionContext> getCurrentlyExecutingJobs() throws SchedulerException {
        return scheduler.getCurrentlyExecutingJobs();
    }

    @Override
    public void setJobFactory(JobFactory factory) throws SchedulerException {
        scheduler.setJobFactory(factory);
    }

    @Override
    public ListenerManager getListenerManager() throws SchedulerException {
        return scheduler.getListenerManager();
    }

    @Override
    public Date scheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        return scheduler.scheduleJob(jobDetail, trigger);
    }

    @Override
    public Date scheduleJob(Trigger trigger) throws SchedulerException {
        return scheduler.scheduleJob(trigger);
    }

    @Override
    public boolean unscheduleJob(TriggerKey triggerKey) throws SchedulerException {
        return scheduler.unscheduleJob(triggerKey);
    }

    @Override
    public boolean unscheduleJobs(List<TriggerKey> triggerKeys) throws SchedulerException {
        return scheduler.unscheduleJobs(triggerKeys);
    }

    @Override
    public Date rescheduleJob(TriggerKey triggerKey, Trigger newTrigger) throws SchedulerException {
        return scheduler.rescheduleJob(triggerKey, newTrigger);
    }

    @Override
    public void addJob(JobDetail jobDetail, boolean replace) throws SchedulerException {
        scheduler.addJob(jobDetail, replace);
    }

    @Override
    public boolean deleteJob(JobKey jobKey) throws SchedulerException {
        return scheduler.deleteJob(jobKey);
    }

    @Override
    public boolean deleteJobs(List<JobKey> jobKeys) throws SchedulerException {
        return scheduler.deleteJobs(jobKeys);
    }

    @Override
    public void triggerJob(JobKey jobKey) throws SchedulerException {
        scheduler.triggerJob(jobKey);

    }

    @Override
    public void triggerJob(JobKey jobKey, JobDataMap data) throws SchedulerException {
        scheduler.triggerJob(jobKey, data);
    }

    @Override
    public void pauseJob(JobKey jobKey) throws SchedulerException {
        scheduler.pauseJob(jobKey);
    }

    @Override
    public void pauseJobs(GroupMatcher<JobKey> matcher) throws SchedulerException {
        scheduler.pauseJobs(matcher);

    }

    @Override
    public void pauseTrigger(TriggerKey triggerKey) throws SchedulerException {
        scheduler.pauseTrigger(triggerKey);

    }

    @Override
    public void pauseTriggers(GroupMatcher<TriggerKey> matcher) throws SchedulerException {
        scheduler.pauseTriggers(matcher);

    }

    @Override
    public void resumeJob(JobKey jobKey) throws SchedulerException {
        scheduler.resumeJob(jobKey);

    }

    @Override
    public void resumeJobs(GroupMatcher<JobKey> matcher) throws SchedulerException {
        scheduler.resumeJobs(matcher);

    }

    @Override
    public void resumeTrigger(TriggerKey triggerKey) throws SchedulerException {
        scheduler.resumeTrigger(triggerKey);

    }

    @Override
    public void resumeTriggers(GroupMatcher<TriggerKey> matcher) throws SchedulerException {
        scheduler.resumeTriggers(matcher);
    }

    @Override
    public void pauseAll() throws SchedulerException {
        scheduler.pauseAll();
    }

    @Override
    public void resumeAll() throws SchedulerException {
        scheduler.resumeAll();
    }

    @Override
    public List<String> getJobGroupNames() throws SchedulerException {
        return scheduler.getJobGroupNames();
    }

    @Override
    public Set<JobKey> getJobKeys(GroupMatcher<JobKey> matcher) throws SchedulerException {
        return scheduler.getJobKeys(matcher);
    }

    @Override
    public List<? extends Trigger> getTriggersOfJob(JobKey jobKey) throws SchedulerException {
        return scheduler.getTriggersOfJob(jobKey);
    }

    @Override
    public List<String> getTriggerGroupNames() throws SchedulerException {
        return scheduler.getTriggerGroupNames();
    }

    @Override
    public Set<TriggerKey> getTriggerKeys(GroupMatcher<TriggerKey> matcher) throws SchedulerException {
        return scheduler.getTriggerKeys(matcher);
    }

    @Override
    public Set<String> getPausedTriggerGroups() throws SchedulerException {
        return scheduler.getPausedTriggerGroups();
    }

    @Override
    public JobDetail getJobDetail(JobKey jobKey) throws SchedulerException {
        return scheduler.getJobDetail(jobKey);
    }

    @Override
    public Trigger getTrigger(TriggerKey triggerKey) throws SchedulerException {
        return scheduler.getTrigger(triggerKey);
    }

    @Override
    public TriggerState getTriggerState(TriggerKey triggerKey) throws SchedulerException {
        return scheduler.getTriggerState(triggerKey);
    }
    
    public void fireTriggerNow(TriggerKey triggerKey) throws SchedulerException {
    	logger.info("Calling trigger now for trigger " + triggerKey.getName());
    	Trigger trigger = scheduler.getTrigger(triggerKey);
    	fireJobNow(trigger.getJobKey());
    }
    
    public void fireJobNow(JobKey jobKey) throws SchedulerException {
    	logger.info("Calling firejob now for jobKey " + jobKey.getName());
    	scheduler.triggerJob(jobKey);
    }

    @Override
    public void addCalendar(String calName, Calendar calendar, boolean replace, boolean updateTriggers) throws SchedulerException {
        scheduler.addCalendar(calName, calendar, replace, updateTriggers);
    }

    @Override
    public boolean deleteCalendar(String calName) throws SchedulerException {
        return scheduler.deleteCalendar(calName);
    }

    @Override
    public Calendar getCalendar(String calName) throws SchedulerException {
        return scheduler.getCalendar(calName);
    }

    @Override
    public List<String> getCalendarNames() throws SchedulerException {
        return scheduler.getCalendarNames();
    }

    @Override
    public boolean interrupt(JobKey jobKey) throws UnableToInterruptJobException {
        return scheduler.interrupt(jobKey);
    }

    @Override
    public boolean interrupt(String fireInstanceId) throws UnableToInterruptJobException {
        return scheduler.interrupt(fireInstanceId);
    }
    

    @Override
    public boolean checkExists(JobKey jobKey) throws SchedulerException {
        return scheduler.checkExists(jobKey);
    }

    @Override
    public boolean checkExists(TriggerKey triggerKey) throws SchedulerException {
        return scheduler.checkExists(triggerKey);
    }

    @Override
    public void clear() throws SchedulerException {
        scheduler.clear();
    }

	@Override
	public void addJob(JobDetail arg0, boolean arg1, boolean arg2)
			throws SchedulerException {
		scheduler.addJob(arg0, arg1, arg2);
		
	}

	@Override
	public void scheduleJob(JobDetail arg0, Set<? extends Trigger> arg1,
			boolean arg2) throws SchedulerException {
		scheduler.scheduleJob(arg0, arg1, arg2);
	}

	@Override
	public void scheduleJobs(Map<JobDetail, Set<? extends Trigger>> arg0,
			boolean arg1) throws SchedulerException {
		scheduler.scheduleJobs(arg0, arg1);
		
	}
}
