package com.simple.orchestrator.hadoop;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ModuleProperties extends Properties {

	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = 3614057311168472970L;

	private static final String MODULE_PROPERTIES_FILE = "/orchestrator-module.properties";

	private static ModuleProperties instance = null;

	private ModuleProperties() {
		InputStream stream = ModuleProperties.class.getResourceAsStream(MODULE_PROPERTIES_FILE);
		try {
			load(stream);
		} catch (IOException e) {
			throw new RuntimeException("Unable to bind module properties with file " + MODULE_PROPERTIES_FILE);
		}
	}

	public static synchronized ModuleProperties getInstance() {
		if (instance == null) {
			instance = new ModuleProperties();
		}
		return instance;
	}
}
