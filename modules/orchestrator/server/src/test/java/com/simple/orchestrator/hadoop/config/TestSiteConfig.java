package com.simple.orchestrator.hadoop.config;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import org.junit.Test;

public class TestSiteConfig {

	
	@Test
	public void testGetValue() throws SiteConfigurationException, URISyntaxException {
		URL url = TestSiteConfig.class.getResource("/configuration/hadoop/core-site.xml");
		SiteConfigEditor editor = SiteConfigEditor.create(Paths.get(url.toURI()));
		
		String value = editor.getValue("fs.defaultFS");
		
		
	}

}
