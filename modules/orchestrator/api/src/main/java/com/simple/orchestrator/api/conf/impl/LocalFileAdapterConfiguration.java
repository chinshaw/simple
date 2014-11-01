package com.simple.orchestrator.api.conf.impl;

import javax.xml.bind.annotation.XmlRootElement;

import com.simple.orchestrator.api.conf.IOutputAdapterConfiguration;

@XmlRootElement
public class LocalFileAdapterConfiguration implements IOutputAdapterConfiguration {

	public static final String OUTPUT_FILE_PATH = "com.artisan.orchestrator.hadoop.job.io.adapter.local.file_output_path";
	
	private final ClientConfiguration configuration;
	
	public LocalFileAdapterConfiguration() {
		this(new ClientConfiguration());
	}
	
	public LocalFileAdapterConfiguration(ClientConfiguration configuration) {
		this.configuration = configuration;
	}
		
	public ClientConfiguration getConfiguration() {
		return configuration;
	}
	
	public String getOutputFileName() {
		return configuration.get(OUTPUT_FILE_PATH);
	}
	
	public void setOutputFileName(String filename) {
		configuration.put(OUTPUT_FILE_PATH, filename);
	}
	
	public static void setLocalFileName(final ClientConfiguration configuration, final String filename) {
		configuration.put(OUTPUT_FILE_PATH, filename);
	}
}