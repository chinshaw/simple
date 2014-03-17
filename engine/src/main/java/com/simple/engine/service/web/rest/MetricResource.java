package com.simple.engine.service.web.rest;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.simple.engine.api.MediaType;
import com.simple.engine.service.hadoop.io.MetricWritable;
import com.simple.original.api.orchestrator.IMetric;

@Path("/metric")
public class MetricResource {

	private final String colFamily;

	private final String colValue;

	private final String colClass;

	private HTable table;

	@Inject
	public MetricResource(@Named("com.artisan.orchestrator.hbase.metric.table") String metricTableName,
			@Named("com.artisan.orchestrator.hbase.metric.colfamily") String colFamily,
			@Named("com.artisan.orchestrator.hbase.metric.colvalue") String colValue,
			@Named("com.artisan.orchestrator.hbase.metric.colclass") String colClass) {

		this.colValue = colValue;
		this.colFamily = colFamily;
		this.colClass = colClass;

		try {
			table = new HTable(new Configuration(), metricTableName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@GET
	@Path("/{rowKey}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_PROTOBUF })
	public IMetric<?> find(@PathParam("rowKey") String rowKey) {
		return find(rowKey, colFamily, colValue);
	}

	@GET
	@Path("/{rowKey}/{column}:{qualifier}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_PROTOBUF })
	public IMetric<?> find(@PathParam("rowKey") String rowKey, @PathParam("column") String column, @PathParam("qualifier") String qualifier) {

		final Get get = new Get(Bytes.toBytes(rowKey));
		byte[] bytes = null;
		byte[] classBytes = null;

		try {
			Result result = table.get(get);
			bytes = result.getValue(Bytes.toBytes(column), Bytes.toBytes(qualifier));

			classBytes = result.getValue(Bytes.toBytes(column), Bytes.toBytes(colClass));
		} catch (IOException e) {
			throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
		}

		if (classBytes == null) {
			Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).tag("class column was not specified for metric")
					.build();
			throw new WebApplicationException(response);
		}

		if (bytes == null) {
			Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).tag("Could not find entity").build();
			throw new WebApplicationException(response);
		}


		
		MetricWritable<IMetric<?>> writable = new MetricWritable<IMetric<?>>();

		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		DataInputStream dais = new DataInputStream(bais);
		
		try {
			writable.readFields(dais);
		} catch (IOException e) {
			throw new WebApplicationException(e);
		}

		return writable.getMetric();
	}
}