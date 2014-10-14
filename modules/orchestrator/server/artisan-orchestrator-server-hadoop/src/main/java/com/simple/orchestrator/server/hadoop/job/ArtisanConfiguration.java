package com.simple.orchestrator.server.hadoop.job;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;

import com.simple.orchestrator.server.ModuleProperties;

/**
 * This will attempt to read the configuration files from the filesytem.
 * 
 * It uses the hadoop and hbase configuration files for configuration.
 * 
 * @author chris
 * 
 */
public class ArtisanConfiguration extends Configuration {

	private static final Log LOG = LogFactory.getLog(ArtisanConfiguration.class);

	public static final String HADOOP_CORE_SITE_CONFIG_XML = "core-site.xml";

	public static final String HADOOP_HDFS_SITE_CONFIG_XML = "hdfs-site.xml";

	public static final String HADOOP_HTTPFS_SITE_CONFIG_XML = "httpfs-site.xml";

	public static final String HADOOP_YARN_SITE_CONFIG_XML = "yarn-site.xml";

	public static final String HADOOP_MAPRED_SITE_CONFIG_XML = "mapred-site.xml";

	public static final String HBASE_SITE_CONFIG_XML = "hbase-site.xml";

	private ModuleProperties properties = ModuleProperties.getInstance();

	/**
	 * Constructor that will load the defaults from the xml configuration files
	 * in the conf directory.
	 */
	public ArtisanConfiguration() {
		loadDefaults();
	}

	/**
	 * This is backwards compatible and does not load defaults it will simply
	 * load your configuration into another configuration.
	 * 
	 * @see Configuration#Configuration(Configuration)
	 * @param other
	 */
	public ArtisanConfiguration(Configuration other) {
		super(other);

	}

	/**
	 * This will parse through the configuration files in the conf/hadoop and
	 * conf/hbase to load the configurations from the file system.
	 */
	public void loadDefaults() {
		String hadoopConfigDir = properties.getProperty(
				"com.artisan.orchestrator.config.dir.hadoop", "/opt/artisan/conf/hadoop");
		String hbaseConfigDir = properties.getProperty("com.artisan.orchestrator.config.dir.hbase",
				"/opt/artisan/conf/hbase");

		try {
			URL coreSiteXml = new URL(String.format("file://%s/%s", hadoopConfigDir,
					HADOOP_CORE_SITE_CONFIG_XML));
			URL hdfsXml = new URL(String.format("file://%s/%s", hadoopConfigDir,
					HADOOP_HDFS_SITE_CONFIG_XML));
			URL httpfsXml = new URL(String.format("file://%s/%s", hadoopConfigDir,
					HADOOP_HTTPFS_SITE_CONFIG_XML));
			URL yarnXml = new URL(String.format("file://%s/%s", hadoopConfigDir,
					HADOOP_YARN_SITE_CONFIG_XML));
			URL mapredXml = new URL(String.format("file://%s/%s", hadoopConfigDir,
					HADOOP_MAPRED_SITE_CONFIG_XML));
			URL hbaseXml = new URL(String.format("file://%s/%s", hbaseConfigDir,
					HBASE_SITE_CONFIG_XML));

			addResource(coreSiteXml);
			addResource(hdfsXml);
			addResource(httpfsXml);
			addResource(yarnXml);
			addResource(mapredXml);
			addResource(hbaseXml);

		} catch (MalformedURLException e) {
			LOG.error("Unable to parse xml configuration file", e);
		}
	}
}