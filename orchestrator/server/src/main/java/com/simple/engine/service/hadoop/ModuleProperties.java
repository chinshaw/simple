package com.simple.engine.service.hadoop;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ModuleProperties extends Properties {
	
	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = 3614057311168472970L;
	
	private static ModuleProperties instance = null;
	
	private ModuleProperties() throws IOException {
		InputStream stream = ModuleProperties.class.getClassLoader().getResourceAsStream("engine.properties");
		load(stream);
	}
	
	
	public static synchronized  ModuleProperties getInstance() throws IOException {
		if (instance == null) {
			instance = new ModuleProperties();
		}
		return instance;
	}
}
