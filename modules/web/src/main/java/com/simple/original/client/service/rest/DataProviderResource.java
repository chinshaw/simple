package com.simple.original.client.service.rest;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestService;
import org.fusesource.restygwt.client.RestServiceProxy;

import com.google.gwt.core.client.GWT;
import com.simple.domain.model.dataprovider.DataProvider;

public interface DataProviderResource extends RestService {

	
	/**
	 * Utility class to get the instance of the Rest Service
	 */
	public static final class Util {

		private static DataProviderResource instance;

		public static final DataProviderResource get() {
			if (instance == null) {
				instance = GWT.create(DataProviderResource.class);
				((RestServiceProxy) instance).setResource(new Resource("http://localhost:8080/simple/r/v1/dataprovider"));
			}
			return instance;
		}
	}
	
	
	@GET
	@Path("/list")
	public void list(MethodCallback<List<DataProvider>> callback);

	@GET
	@Path("/page")
	public void page(@QueryParam("start") int start,
			@QueryParam("max") int max,
			MethodCallback<List<DataProvider>> callback);

	@GET
	@Path("/{id}")
	public void find(@PathParam("id") String pathId,
			MethodCallback<DataProvider> callback);
	
	@PUT
	public void create(DataProvider dataProvider,
			MethodCallback<DataProvider> callback);

	@POST
	public void update(DataProvider dataProvider, MethodCallback<Long> callback);

	@DELETE
	@Path("/{id}")
	public void delete(@PathParam("id") Long id, MethodCallback<Void> callback);
}