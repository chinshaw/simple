package com.simple.original.server.service.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.simple.domain.model.ui.dashboard.Dashboard;
import com.simple.domain.model.ui.dashboard.DashboardDao;
import com.simple.original.api.exceptions.DomainException;


@Path("/dashboard")
public class DashboardResource {

	@Inject 
	private DashboardDao dao;
	
	@GET
	@Path("/list")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public List<Dashboard> list() {
		return dao.listAll();
	}
	
	@GET
	@Path("/page")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public List<Dashboard> list(@QueryParam("start") int start, @QueryParam("max") int max) {
		return dao.findRange(start, max);
	}
	
	@GET
	@Path("/{id}")
	public Dashboard find(@PathParam("id") String pathId) {
		if (pathId == null) {
			throw new IllegalArgumentException("Id was null");
		}
		try {
			Long id = Long.parseLong(pathId);
			System.out.println("Looking for data provider with id" + id);
			return dao.find(id);
		} catch (NumberFormatException ne) {
			throw new IllegalArgumentException("Invalid id");
		}
	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Dashboard create(Dashboard dashboard) throws DomainException {
		return dao.create(dashboard);
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Long update(Dashboard dashboard) throws DomainException {
		return dao.save(dashboard);
	}
	
	@DELETE
	@Path("/{id}")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public void delete(@PathParam("id") Long id) {
		dao.delete(id);
	}
}