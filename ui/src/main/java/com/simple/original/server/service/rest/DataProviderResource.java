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
import com.simple.domain.dao.DataProviderDao;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.original.api.exceptions.DomainException;


@Path("/dataprovider")
public class DataProviderResource {

	@Inject 
	private DataProviderDao dao;
	
	@GET
	@Path("/list")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public List<DataProvider> list() {
		return dao.listAll();
	}
	
	@GET
	@Path("/page")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public List<DataProvider> list(@QueryParam("start") int start, @QueryParam("max") int max) {
		return dao.findRange(start, max);
	}
	
	@GET
	@Path("/{id}")
	public DataProvider find(@PathParam("id") String pathId) {
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
	public DataProvider create(DataProvider dataProvider) throws DomainException {
		return dao.create(dataProvider);
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Long update(DataProvider dataProvider) throws DomainException {
		return dao.save(dataProvider);
	}
	
	@DELETE
	@Path("/{id}")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public void delete(@PathParam("id") Long id) {
		dao.delete(id);
	}
}