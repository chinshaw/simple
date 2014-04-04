package com.artisan.utils;

import java.io.IOException;
import java.io.InputStream;

public class Properties extends java.util.Properties {

	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = 164177012484176760L;
	
	public Properties() {
		
	}
	
	public Properties(String propertiesFile) throws IOException {
		load(propertiesFile);
	}
	
	public void load(String propertiesFile) throws IOException {
		InputStream stream = Properties.class.getClassLoader().getResourceAsStream(propertiesFile);
        load(stream);
	}
}
