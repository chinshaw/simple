package com.simple.orchestrator.service.web.rest;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import com.simple.api.orchestrator.IMetric;
import com.simple.orchestrator.api.rest.MediaType;
import com.simple.orchestrator.metric.Metric;
import com.simple.orchestrator.service.hadoop.io.MetricWritable;

@Path("/metric")
public class MetricResource {

	private static final Logger logger = Logger.getLogger(MetricResource.class.getName());

	private final String metricTableName;

	private final String colFamily;

	private final String colValue;

	private final String colClass;

	@Inject
	public MetricResource(@Named("com.artisan.orchestrator.hbase.metric.table") String metricTableName,
			@Named("com.artisan.orchestrator.hbase.metric.colfamily") String colFamily,
			@Named("com.artisan.orchestrator.hbase.metric.colvalue") String colValue,
			@Named("com.artisan.orchestrator.hbase.metric.colclass") String colClass) {

		this.metricTableName = metricTableName;
		this.colValue = colValue;
		this.colFamily = colFamily;
		this.colClass = colClass;

	}

	@GET
	@Path("/{rowKey}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_PROTOBUF })
	public IMetric find(@PathParam("rowKey") String rowKey) {
		return find(rowKey, colFamily, colValue);
	}

	@GET
	@Path("/{rowKey}/{column}:{qualifier}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_PROTOBUF })
	public IMetric find(@PathParam("rowKey") String rowKey, @PathParam("column") String column, @PathParam("qualifier") String qualifier) {

		final Get get = new Get(Bytes.toBytes(rowKey));
		byte[] bytes = null;
		byte[] classBytes = null;

		HTable table;
		try {
			table = new HTable(createConfiguration(), metricTableName);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Unable to connect to hbase", e);
			throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
		}
		
		MetricWritable<Metric<?>> writable = null;

		try {
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

			writable = new MetricWritable<Metric<?>>();
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			DataInputStream dais = new DataInputStream(bais);

			try {
				writable.readFields(dais);
			} catch (IOException e) {
				throw new WebApplicationException(e);
			}
		} finally {
			if (table != null) {
				try {
					table.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, "Unable to close htable", e);
				}	
			}
		}

		return writable.getMetric();
	}

	
	static Configuration createConfiguration() {
		Configuration conf = new Configuration();
		
		return conf;
	}
}