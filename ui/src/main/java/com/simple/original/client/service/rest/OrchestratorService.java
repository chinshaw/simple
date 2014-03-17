package com.simple.original.client.service.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestService;
import org.fusesource.restygwt.client.RestServiceProxy;

import com.google.gwt.core.client.GWT;

public interface OrchestratorService extends RestService  {

	
	/**
	 * Utility class to get the instance of the Rest Service
	 */
	public static final class Util {

		private static OrchestratorService instance;

		public static final OrchestratorService get(String host, String port) {
			if (instance == null) {
				instance = GWT.create(OrchestratorService.class);
				((RestServiceProxy) instance).setResource(new Resource("http://localhost:8080/simple/"));
			}
			return instance;
		}
	}
	

	@GET
	@Options(timeout = 5000)
	@Path("/orc/metric/{row}/")
	public void fetchMetric(@PathParam("row") String row, MethodCallback<Metric> callback);
}
