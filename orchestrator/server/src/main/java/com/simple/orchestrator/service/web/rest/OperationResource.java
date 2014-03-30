package com.simple.orchestrator.service.web.rest;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.inject.Inject;
import com.simple.api.orchestrator.IMetric;
import com.simple.orchestrator.api.MediaType;
import com.simple.orchestrator.service.AnalyticsOperationException;
import com.simple.orchestrator.service.hadoop.config.ConfigurationException;
import com.simple.orchestrator.service.hadoop.job.OperationJob;
import com.simple.orchestrator.service.hadoop.mrv2.OperationDriver;

@Path("/operation")
public class OperationResource {

	private final OperationDriver driver;

	@Inject
	public OperationResource(OperationDriver driver) {
		this.driver = driver;
	}

	@POST
	@Path("/execute")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_PROTOBUF })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_PROTOBUF })
	public HashMap<Long, IMetric> execute(OperationJob operationJob) throws AnalyticsOperationException, ConfigurationException {
		
		return driver.execute(operationJob.getOwner(), operationJob.getUserInputs(), operationJob.getOperation(),
				operationJob.getDataProviders());
	}
}
