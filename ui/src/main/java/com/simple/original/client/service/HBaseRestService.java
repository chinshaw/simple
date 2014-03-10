package com.simple.original.client.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestService;
import org.fusesource.restygwt.client.RestServiceProxy;

import com.google.gwt.core.client.GWT;

public interface HBaseRestService extends RestService {

	/**
	 * Utility class to get the instance of the Rest Service
	 */
	public static final class Util {

		private static HBaseRestService instance;

		public static final HBaseRestService get(String host, String port) {
			if (instance == null) {
				instance = GWT.create(HBaseRestService.class);
				((RestServiceProxy) instance).setResource(new Resource("http://localhost:8080/simple/"));
			}
			return instance;
		}
	}

	@GET
	@Options(timeout = 5000)
	@Path("/hbase/{table}/{row}/{columnFamily}:{qualifier}")
	public void query(@PathParam("table") String table, @PathParam("row") String row, @PathParam("columnFamily") String columnFamily,
			@PathParam("qualifier") String qualifier, MethodCallback<String> callback);
}
