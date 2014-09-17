package com.simple.orchestrator.server.service.web.rest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.simple.orchestrator.server.hadoop.config.SiteConfigEditor;
import com.simple.orchestrator.server.hadoop.config.SiteConfigurationException;

@Path("/hadoop")
public class HadoopResource {

	private String hdfsFileName;

	private String coreSiteXml;

	@Inject
	public HadoopResource(@Named("com.artisan.hadoop.hdfs.file.path") String coreSiteXml) {
		this.coreSiteXml = coreSiteXml;
	}

	@GET
	@Path("/config/hdfs-site")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String fetchHdfsSiteConfig() throws IOException {

		if (hdfsFileName == null) {
			throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.tag("Missing property hadoop.hdfs.file.path").build());
		}

		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(hdfsFileName));
		} catch (FileNotFoundException e) {
			throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.tag("Unable to open hdfs config file " + hdfsFileName).build());
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
			throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.tag("Unable to read from hdfs config file " + hdfsFileName + " cause => " + e.getMessage()).build());
		} finally {
			br.close();
		}
	}

	public String getCoreSiteConfig(String name) throws SiteConfigurationException {
		return SiteConfigEditor.create(Paths.get(coreSiteXml)).getValue(name);
	}

	public void setCoreSiteConfig(String name, String value) {

	}

	public String getHdfsSiteConfig(String name) {
		return null;
	}

	public void setHdfsSiteConfig(String key, String value) {

	}

	public String getMapredSiteConfig(String name) {
		return null;
	}

	public void setMapredSiteConfig(String name, String value) {

	}

	public String getYarnSiteConfig(String name) {
		return null;
	}

	public void setYarnSiteConfig(String name, String value) {

	}
}
