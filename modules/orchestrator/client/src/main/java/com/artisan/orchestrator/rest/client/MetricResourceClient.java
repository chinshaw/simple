package com.artisan.orchestrator.rest.client;

import java.util.List;

import javax.ws.rs.core.MediaType;

import com.simple.api.orchestrator.IMetric;
import com.simple.orchestrator.api.metric.Metric;
import com.simple.orchestrator.api.service.IMetricService;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

public class MetricResourceClient implements IMetricService {

	public static final String SERVICE_PATH = "metric";

	private final WebResource resource;

	/**
	 * Default Constructor
	 * @param client The Artisan client to use
	 * @param baseUrl Base url for the service
	 */
	public MetricResourceClient(ArtisanClient client, String baseUrl) {
		resource = client.resource(baseUrl + "/" + SERVICE_PATH);
		resource.accept(MediaType.APPLICATION_JSON);
	}

	/**
	 * 
	 * 
	 * 
	 * @GET
	 * @Path("/{rowKey ")
	 * @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,
	 *             MediaType.APPLICATION_PROTOBUF })
	 */
	@Override
	public Metric<?> find(String rowKey) {
		Metric<?> metric = resource.path(rowKey).type(MediaType.APPLICATION_JSON)
				.get(Metric.class);
		return metric;
	}

	/**
	 * Implementation for fetching a specify value by it's row key column and
	 * qualifier
	 * 
	 * @GET
	 * @Path("/{rowKey /{column}:{qualifier}")
	 * @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,
	 *             MediaType.APPLICATION_PROTOBUF })
	 */
	@Override
	public Metric<?> find(String rowKey, String column, String qualifier) {
		return resource.path(rowKey).path(column + ":" + qualifier)
				.get(Metric.class);
	}

	@Override
	public List<IMetric> find(Long operationId) {
		List<IMetric> metrics = resource.path("operation")
				.path(operationId.toString())
				.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<IMetric>>(){});
		return metrics;
	}
}
