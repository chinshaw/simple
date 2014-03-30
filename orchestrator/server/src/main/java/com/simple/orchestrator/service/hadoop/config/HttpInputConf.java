package com.simple.orchestrator.service.hadoop.config;

import org.apache.hadoop.conf.Configuration;

public class HttpInputConf {

	public static final String WEB_URL_PROPERTY = "mapreduce.http.input.url";

	private Configuration configuration;
	
	public HttpInputConf(Configuration configuration) {
		this.configuration = configuration;
	}
	
	
	public void setWebUrl(String url) {
		configuration.set(WEB_URL_PROPERTY, url);
	}
	
	public String getWebUrl() {
		return configuration.get(WEB_URL_PROPERTY);
	}
}
