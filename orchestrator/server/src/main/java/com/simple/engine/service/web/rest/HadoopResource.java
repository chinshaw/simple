package com.simple.engine.service.web.rest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.name.Named;

@Path("/hadoop")
public class HadoopResource {

	@Named("com.simple.hadoop.hdfs.file.path")
	private String hdfsFileName;

	@GET
	@Path("/config/hdfs-site")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public String fetchHdfsSiteConfig() throws IOException {

		if (hdfsFileName == null) {
			throw new WebApplicationException(Response.status(
					Response.Status.INTERNAL_SERVER_ERROR).tag(
					"Missing property hadoop.hdfs.file.path").build());
		}
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(hdfsFileName));
		} catch (FileNotFoundException e) {
			throw new WebApplicationException(Response.status(
					Response.Status.INTERNAL_SERVER_ERROR).tag(
					"Unable to open hdfs config file " + hdfsFileName).build());
		}
		try {
			StringBuilder sb = new StringBuilder();
			String line = null;

			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
			}

			return sb.toString();
			
		} catch (IOException e) {
			throw new WebApplicationException(Response.status(
					Response.Status.INTERNAL_SERVER_ERROR).tag(
					"Unable to read from hdfs config file " + hdfsFileName + " cause => " + e.getMessage()).build());
		} finally {
			br.close();
		}
	}
}
