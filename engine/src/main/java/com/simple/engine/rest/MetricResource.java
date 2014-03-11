package com.simple.engine.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
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

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.simple.engine.api.IMetric;
import com.simple.engine.metric.MetricRaw;

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

	@GET
	@Path("/{row}/{column}:{qualifier}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/x-protobuf"})
	public IMetric<?> findMetric(@PathParam("row") String row,
			@PathParam("column") String column,
			@PathParam("qualifier") String qualifier, @Context HttpServletRequest httpRequest) {
		System.out.println("requesting row " + row + " column " + column + " qual " + qualifier);
		Get get = new Get(Bytes.toBytes(row));
		//get.addFamily(Bytes.toBytes(column));
		//get.addColumn(Bytes.toBytes(column), Bytes.toBytes(qualifier));
		Result result;
		byte[] bytes = null;
		try {
			result = table.get(get);
			bytes = result.getValue(Bytes.toBytes(column), Bytes.toBytes(qualifier));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if ( bytes == null) {
			throw new RuntimeException("Unable to find bytes");
		}
		
		MetricRaw metric = new MetricRaw();
		ProtobufIOUtil.mergeFrom(bytes, metric, MetricRaw.SCHEMA);
		System.out.println("Metric key is " + metric.getKey());
		
		return metric;
	}
}