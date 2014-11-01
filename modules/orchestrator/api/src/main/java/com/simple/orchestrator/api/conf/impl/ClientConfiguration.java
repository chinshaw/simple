package com.simple.orchestrator.api.conf.impl;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
public class ClientConfiguration {
	
	private Map<String, String> properties;

	public void put(String key, String value) {
		properties.put(key, value);
	}
	
	public String get(String key) {
		return properties.get(key);
	}
	
	@XmlJavaTypeAdapter(MapAdapter.class)
	public Map<String, String> getProperties() {
		return properties;
	}
}