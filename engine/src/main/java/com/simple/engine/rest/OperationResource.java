package com.simple.engine.rest;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.simple.engine.api.IOperationJob;
import com.simple.engine.api.MediaType;
import com.simple.engine.service.AnalyticsOperationException;
import com.simple.engine.service.hadoop.config.ConfigurationException;
import com.simple.engine.service.hadoop.mrv2.OperationDriver;
import com.simple.original.api.analytics.IMetric;

@Path("/operation")
public class OperationResource {

	private static final OperationDriver executor = new OperationDriver();

	@POST
	@Path("/execute")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,  MediaType.APPLICATION_PROTOBUF})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,  MediaType.APPLICATION_PROTOBUF})
	public HashMap<Long, IMetric> execute(IOperationJob operationJob)
			throws AnalyticsOperationException, ConfigurationException {
		return executor.execute(operationJob.getOwner(), operationJob.getUserInputs(),
				operationJob.getOperation(), operationJob.getDataProviders());
	}
}
