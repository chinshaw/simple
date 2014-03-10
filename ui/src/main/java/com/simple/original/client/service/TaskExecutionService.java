package com.simple.original.client.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestService;
import org.fusesource.restygwt.client.RestServiceProxy;

import com.google.gwt.core.client.GWT;
import com.simple.original.api.analytics.ITaskExecution;

public interface TaskExecutionService extends RestService {

	/**
     * Utility class to get the instance of the Rest Service
     */
    public static final class Util {

        private static TaskExecutionService instance;

        public static final TaskExecutionService get() {
            if (instance == null) {
                instance = GWT.create(TaskExecutionService.class);
                ((RestServiceProxy) instance).setResource(new Resource(
                		GWT.getHostPageBaseURL() +
                        "rest/v1/taskexecution"));
            }
            return instance;
        }
        
        public static final TaskExecutionService get(String host) {
            if (instance == null) {
                instance = GWT.create(TaskExecutionService.class);
                ((RestServiceProxy) instance).setResource(new Resource(
                		host +
                        "rest/v1/taskexecution"));
            }
            return instance;
        }


        private Util() {

        }
    }
	
	
	@GET
	@Path("/address/{tn}")
	public void getTaskExecution(@PathParam("tn")String tn, MethodCallback<ITaskExecution> callback);
	

	
        
}