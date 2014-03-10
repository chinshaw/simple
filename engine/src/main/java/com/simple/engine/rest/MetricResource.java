package com.simple.engine.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.simple.engine.api.IMetric;

@Path("/metrics")
public class MetricResource {

	private static final String TABLE_NAME = "metrics";
	
	private static HTable table;
	static {
		try {
			table = new HTable(new Configuration(), TABLE_NAME);
		} catch (IOException e) {
			throw new RuntimeException("Unable to connect to hbase table "
					+ TABLE_NAME);
		}
	}

	@Path("/{row}/{column}:{qualifier}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/x-protobuf"})
	public IMetric<?> findMetric(@PathParam("row") String row,
			@PathParam("column") String column,
			@PathParam("qualifier") String qualifier, @Context HttpServletRequest httpRequest) throws IOException {
		Get get = new Get(Bytes.toBytes(row));
		get.addFamily(Bytes.toBytes(qualifier));
		get.setMaxVersions(1);
		Result result = table.get(get);

		return null;
	}
}