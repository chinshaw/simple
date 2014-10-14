package com.simple.orchestrator.server.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public interface JobLoggingListener {

    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException);

}
