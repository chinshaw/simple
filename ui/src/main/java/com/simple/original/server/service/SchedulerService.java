package com.simple.original.server.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.UnableToInterruptJobException;

import com.google.inject.Inject;
import com.simple.domain.AnalyticsOperationInput;
import com.simple.domain.AnalyticsTask;
import com.simple.domain.dao.AnalyticsTaskDao;
import com.simple.engine.scheduler.RExecutionJob;
import com.simple.engine.scheduler.TaskEngineClient;
import com.simple.original.api.exceptions.AnalyticsTaskException;
import com.simple.original.server.scheduler.JobExecutionContext;

public class SchedulerService {
    

	private static AnalyticsTaskDao taskDao;
    
    private static TaskEngineClient client;
    
    @Inject
    public SchedulerService(AnalyticsTaskDao taskDao, TaskEngineClient client) {
    	SchedulerService.taskDao = taskDao;
    	SchedulerService.client = client;
    }
    
    /**
     * This uses the cron format to schedule a job to be run at a future date.
     * 
     * An example of running a job every 5 seconds would be "/5 * * ? * *"
     * 
     * Here is more documentation on the CronTrigger
     * http://www.quartz-scheduler.
     * org/documentation/quartz-1.x/tutorials/crontrigger
     * 
     * Expression Meaning 0 0 12 * * ? Fire at 12pm (noon) every day 0 15 10 ? *
     * * Fire at 10:15am every day 0 15 10 * * ? Fire at 10:15am every day 0 15
     * 10 * * ? * Fire at 10:15am every day 0 15 10 * * ? 2005 Fire at 10:15am
     * every day during the year 2005 0 * 14 * * ? Fire every minute starting at
     * 2pm and ending at 2:59pm, every day 0 0/5 14 * * ? Fire every 5 minutes
     * starting at 2pm and ending at 2:55pm, every day 0 0/5 14,18 * * ? Fire
     * every 5 minutes starting at 2pm and ending at 2:55pm, AND fire every 5
     * minutes starting at 6pm and ending at 6:55pm, every day 0 0-5 14 * * ?
     * Fire every minute starting at 2pm and ending at 2:05pm, every day 0 10,44
     * 14 ? 3 WED Fire at 2:10pm and at 2:44pm every Wednesday in the month of
     * March. 0 15 10 ? * MON-FRI Fire at 10:15am every Monday, Tuesday,
     * Wednesday, Thursday and Friday 0 15 10 15 * ? Fire at 10:15am on the 15th
     * day of every month 0 15 10 L * ? Fire at 10:15am on the last day of every
     * month 0 15 10 ? * 6L Fire at 10:15am on the last Friday of every month 0
     * 15 10 ? * 6L Fire at 10:15am on the last Friday of every month 0 15 10 ?
     * * 6L 2002-2005 Fire at 10:15am on every last friday of every month during
     * the years 2002, 2003, 2004 and 2005 0 15 10 ? * 6#3 Fire at 10:15am on
     * the third Friday of every month 0 0 12 1/5 * ? Fire at 12pm (noon) every
     * 5 days every month, starting on the first day of the month. 0 11 11 11 11
     * ? Fire every November 11th at 11:11am.
     * 
     * @param analyticsTaskId
     *            The analytics task id of the analytics task that will be run.
     * @param inputs
     *            User inputs from the analytics task scheduler that will be
     *            sent to the execution cycle.
     * @param cronExpression
     *            The cron expression used to schedule the job.
     * @param description
     *            User defined description of what the job will do, this should
     *            be as descriptive as possible.
     * @throws AnalyticsTaskException
     *             This is a wrapper exeception and the cause should be checked
     *             for a more valid error condition.
     */
    public static void scheduleAnalyticsTask(Long analyticsTaskId, List<AnalyticsOperationInput> inputs, String cronExpression, String description, Date startDate) throws AnalyticsTaskException {
        AnalyticsTask analyticsTask = taskDao.find(analyticsTaskId);
        // define the job and ask it to run
        String analyticsTaskIdentity = analyticsTask.getName() + " " + new Date();

        JobDetail job = JobBuilder.newJob(RExecutionJob.class).withIdentity(analyticsTaskIdentity, "rscripts").build();

        JobDataMap map = job.getJobDataMap();
        map.put(RExecutionJob.INPUTS, inputs);
        map.put(RExecutionJob.ANALYTICS_TASK_ID, analyticsTaskId);

        try {
        	TriggerBuilder<CronTrigger> triggerBuilder = TriggerBuilder.newTrigger().withIdentity(analyticsTaskIdentity, "Analytics").withDescription(description).forJob(job.getKey()).withSchedule(CronScheduleBuilder.cronSchedule(cronExpression));
        	
        	if (startDate != null) {
        		triggerBuilder.startAt(startDate);
        	}
        	
            getTaskEngine().scheduleJob(job, triggerBuilder.build());

        } catch (SchedulerException e) {
            throw new AnalyticsTaskException("Unable to schedule  your job", e);
        }
    }

    public static TaskEngineClient getTaskEngine() throws AnalyticsTaskException, SchedulerException {
       return client;
    }

    public static void unscheduleJob(String keyName, String groupName) throws AnalyticsTaskException {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(keyName, groupName);
            getTaskEngine().unscheduleJob(triggerKey);
        } catch (SchedulerException e) {
            throw new AnalyticsTaskException("Unable to unschedule your job with key " + keyName, e);
        }
    }

    public static List<Trigger> getScheduledJobs() throws AnalyticsTaskException {
        try {
            return getTaskEngine().listScheduledJobs();
        } catch (SchedulerException e) {
            throw new AnalyticsTaskException("Error while communicating with scheduler", e);
        }
    }
    
    public static List<Trigger> getScheduledJobs(int start, int max) throws AnalyticsTaskException {
        try {
            List<Trigger> allTriggers = getTaskEngine().listScheduledJobs();
            if (max > allTriggers.size()) {
                max = allTriggers.size();
            }
            return allTriggers.subList(start, max);
        } catch (SchedulerException e) {
            throw new AnalyticsTaskException("Error while communicating with scheduler", e);
        }
    }

    public static void pauseTrigger(TriggerKey triggerKey) throws AnalyticsTaskException, SchedulerException {
        getTaskEngine().pauseTrigger(triggerKey);
    }

    public static void resumeTrigger(TriggerKey triggerKey) throws AnalyticsTaskException, SchedulerException {
        getTaskEngine().resumeTrigger(triggerKey);
    }

    public static Trigger getTrigger(String keyName, String groupName) throws AnalyticsTaskException, SchedulerException {
        return getTaskEngine().getTrigger(TriggerKey.triggerKey(keyName, groupName));
    }
    
    public static void fireTriggerNow(TriggerKey triggerKey) throws AnalyticsTaskException, SchedulerException {
    	getTaskEngine().fireTriggerNow(triggerKey);
    }
    
    public static void fireJobNow(JobKey jobKey) throws AnalyticsTaskException, SchedulerException {
    	getTaskEngine().fireJobNow(jobKey);
    }
    
    
    public static Date rescheduleJob(TriggerKey triggerKey, Trigger trigger) throws AnalyticsTaskException, SchedulerException {
        return getTaskEngine().rescheduleJob(triggerKey, trigger);
    }
    
    public static String getTriggerState(TriggerKey triggerKey) throws AnalyticsTaskException, SchedulerException {
        return getTaskEngine().getTriggerState(triggerKey).name();
    }
    
    public static List<JobExecutionContext> getAllRunningJobs() throws AnalyticsTaskException, SchedulerException {
        List<org.quartz.JobExecutionContext> runningJobs = getTaskEngine().getCurrentlyExecutingJobs();
        
        List<JobExecutionContext> adapters = new ArrayList<JobExecutionContext>();
        for (org.quartz.JobExecutionContext runningJob : runningJobs) {
            adapters.add(new JobExecutionContext(runningJob));
        }
        
        return adapters;
    }
    
    public static boolean cancelRunningTask(JobKey jobKey) throws UnableToInterruptJobException, AnalyticsTaskException, SchedulerException {
        return getTaskEngine().interrupt(jobKey);
    }
    
    public static List<Trigger> searchSchedules(String searchString) throws AnalyticsTaskException {
    	List<Trigger> foundTriggers = new ArrayList<Trigger>();
    	
    	List<Trigger> allTriggers = getScheduledJobs();
    	
    	for (Trigger trigger: allTriggers) {
    		if ( trigger.getDescription().toLowerCase().contains(searchString.toLowerCase()) ) {
    			foundTriggers.add(trigger);
    		}
    	}
    	
    	return foundTriggers;
    	
    }
    
    public static AnalyticsTask getTaskForTrigger(JobKey jobKey) throws AnalyticsTaskException, SchedulerException {
        JobDetail jobDetail = getTaskEngine().getJobDetail(jobKey);
        
        JobDataMap map = jobDetail.getJobDataMap();
        Long taskId = map.getLong(RExecutionJob.ANALYTICS_TASK_ID);
        
        return taskDao.find(taskId);
    }
}