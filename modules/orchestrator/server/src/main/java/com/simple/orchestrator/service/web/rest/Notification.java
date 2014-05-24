package com.simple.orchestrator.service.web.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.hadoop.mapreduce.JobID;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.simple.orchestrator.api.event.JobCompletionEvent;

/**
 * This is used for anonymous notification services such as the cluster
 * 
 * @author chris
 */
@Path("/notification")
public class Notification {

	private EventBus eventBus;

	/**
	 * Injected constructor that takes the event bus, this is used
	 * to send all notifications throughout the application.
	 * @param eventBus EventBus to use.
	 */
	@Inject
	public Notification(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	/**
	 * Helper to alert when a job complets, it uses the hadoop parameter job.end.notification.url to
	 * send the job notification to this url. This method will create the {@link JobCompletionEvent} 
	 * and put it out on the eventbus.
	 * @param jobid The {@link JobID} for the submitted job
	 * @param status The status of the job SUCESSFUL or FAILED
	 * @return Response 200 always because this is an internal hadoop method.
	 */
	@GET
	@Path("/hadoop/completed/{jobId}")
	public Response onJobCompletion(@PathParam("jobId") String jobid, @QueryParam("status") String status) {
		JobCompletionEvent jobCompletion = new JobCompletionEvent(jobid, status);
		eventBus.post(jobCompletion);
		return Response.ok().build();
	}
}