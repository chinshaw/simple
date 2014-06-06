package com.artisan.orchestrator.rest.client;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import com.simple.api.orchestrator.IMetric;
import com.simple.orchestrator.api.metric.Metric;
import com.simple.orchestrator.api.service.IMetricService;

public class MetricService implements IMetricService {


	private final WebTarget webTarget;

	public MetricService(Client client, String baseUrl) {
		webTarget = client.target(baseUrl + "/" + IMetricService.REST_BASE_URL);
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
		Metric<?> metric = webTarget.path(rowKey).request(MediaType.APPLICATION_JSON)
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
		return webTarget.path(rowKey)
				.path(column + ":" + qualifier)
				.request(MediaType.APPLICATION_JSON)
				.get(Metric.class);
	}

	@Override
	public List<IMetric> find(Long operationId) {
		List<IMetric> metrics = webTarget.path("operation")
				.path(operationId.toString())
				.request(MediaType.APPLICATION_JSON).get(new GenericType<List<IMetric>>(){});
		return metrics;
	}
}
