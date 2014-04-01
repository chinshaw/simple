package com.simple.orchestrator.service.web.rest;

import java.util.List;

import javax.ws.rs.Consumes;
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
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.orchestrator.api.IJobProgress;
import com.simple.orchestrator.api.IOperationExecutionResponse;
import com.simple.orchestrator.api.IOperationExecutionService;
import com.simple.orchestrator.api.IOperationJob;
import com.simple.orchestrator.api.exception.InvalidJobIdException;
import com.simple.orchestrator.api.exception.JobException;
import com.simple.orchestrator.api.rest.MediaType;
import com.simple.orchestrator.service.AnalyticsOperationException;
import com.simple.orchestrator.service.hadoop.config.ConfigurationException;
import com.simple.orchestrator.service.hadoop.mrv2.OperationDriver;

@Path("/operation")
public class OperationResource implements IOperationExecutionService {

	private final OperationDriver driver;

	private final AnalyticsOperationDao dao;

	@Inject
	public OperationResource(OperationDriver driver, AnalyticsOperationDao dao) {
		this.driver = driver;
		this.dao = dao;
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_PROTOBUF })
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
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_PROTOBUF })
	public List<AnalyticsOperation> page(@QueryParam("start") int start, @QueryParam("max") int max) throws DomainException {
		return dao.findRange(start, max);
	}
	
	
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_PROTOBUF })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_PROTOBUF })
	public AnalyticsOperation put(AnalyticsOperation operation) throws DomainException {
		return update(operation);
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_PROTOBUF })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_PROTOBUF })
	public AnalyticsOperation update(AnalyticsOperation operation) throws DomainException {
		return dao.saveAndReturn(operation);
	}
	
	@POST
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_PROTOBUF })
	public void delete(@PathParam("id") String id) throws DomainException {
		dao.delete(Long.parseLong(id)); 
	}
	
	
	@Override
	public IOperationExecutionResponse executeSynchronous(IOperationJob operationJob) {
		// TODO Auto-generated method stub
		return null;
	}

	@POST
	@Path("/execute")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_PROTOBUF })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_PROTOBUF })
	public String execute(IOperationJob operationJob) throws JobException {
		try {
			List<AnalyticsOperationInput> inputs = (List<AnalyticsOperationInput>) operationJob.getUserInputs();
			List<DataProvider> dataProviders = (List<DataProvider>) operationJob.getDataProviders();
			driver.execute(operationJob.getOwner(), inputs, operationJob.getOperation(), dataProviders);
		} catch (AnalyticsOperationException | ConfigurationException e) {
			throw new JobException("Unable to execute operation job ", e);
		}

		return "FOO";
	}

	@Override
	public void stop(String jobId) throws InvalidJobIdException {
		driver.attemptStop(jobId);

	}

	@Override
	public IJobProgress progress(String jobId) throws InvalidJobIdException {
		// TODO Auto-generated method stub
		return null;
	}
}
