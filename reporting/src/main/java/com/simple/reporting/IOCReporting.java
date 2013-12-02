package com.simple.reporting;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

public class IOCReporting extends AbstractModule {

	@Override
	protected void configure() {
		Names.bindProperties(binder(), getProperties());
		bind(SmtpUtils.class);
	}
	
	@Provides
	@Singleton
	Properties getProperties() {
		Properties props = new Properties();
		
		try {
			InputStream stream = IOCReporting.class.getClassLoader().getResourceAsStream("reporting.properties");
			props.load(stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return props;
	}
}
