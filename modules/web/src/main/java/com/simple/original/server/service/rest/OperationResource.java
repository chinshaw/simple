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

import com.google.inject.Inject;
import com.simple.api.exceptions.DomainException;
import com.simple.domain.dao.AnalyticsOperationDao;
import com.simple.domain.model.AnalyticsOperation;
import com.simple.orchestrator.api.rest.MediaType;

/**
 * Rest api to manage storing and retrieving operations from the
 * data store.
 * @author chris
 */
@Path("/operation")
public class OperationResource {

	private final AnalyticsOperationDao dao;

	@Inject
	public OperationResource(AnalyticsOperationDao dao) {
		this.dao = dao;
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_PROTOBUF })
	public AnalyticsOperation get(@PathParam("id") String id) {
		try {
			Long dbId = Long.parseLong(id);
			return dao.find(dbId);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("id must be of type long");
		}
	}

	@GET
	@Path("/page")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_PROTOBUF })
	public List<AnalyticsOperation> page(@QueryParam("start") int start,
			@QueryParam("max") int max) throws DomainException {
		return dao.findRange(start, max);
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_PROTOBUF })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_PROTOBUF })
	public AnalyticsOperation put(AnalyticsOperation operation)
			throws DomainException {
		return update(operation);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_PROTOBUF })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_PROTOBUF })
	public AnalyticsOperation update(AnalyticsOperation operation)
			throws DomainException {
		return dao.saveAndReturn(operation);
	}

	@DELETE
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_PROTOBUF })
	public void delete(@PathParam("id") String id) throws DomainException {
		dao.delete(Long.parseLong(id));
	}
}
