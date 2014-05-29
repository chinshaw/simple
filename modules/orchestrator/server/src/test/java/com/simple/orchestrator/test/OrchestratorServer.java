package com.simple.orchestrator.test;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.util.Bytes;

import com.simple.orchestrator.hadoop.ModuleProperties;

public class OrchestratorServer {

	private static final Logger logger = Logger.getLogger(OrchestratorServer.class.getName());

	private static final ModuleProperties props = ModuleProperties.getInstance();

	private static final String METRIC_TABLE = props.getProperty("com.artisan.orchestrator.hbase.metric.table");

	private static final String METRIC_TABLE_COLUMN_FAMILY = props.getProperty("com.artisan.orchestrator.hbase.metric.colfamily");

	private Tomcat tomcat = new Tomcat();

	public void start() throws LifecycleException, InterruptedException, ServletException, IOException {
		logger.info("Starting");
		
		doConfigureHbase();
		doStartWebServer();
		
		logger.info("Orchestrator Server: Started");
	}

	public void stop() throws LifecycleException {
		tomcat.getServer().stop();
	}

	public static void main(String[] args) throws LifecycleException, InterruptedException, ServletException, IOException {
		OrchestratorServer orchestrator = new OrchestratorServer();
		orchestrator.start();
	}
	
	
	private void doStartWebServer() throws IOException, ServletException, LifecycleException {
		logger.info("Starting web server");
		String currentDir = new File(".").getCanonicalPath();

		String webRoot = currentDir + File.separatorChar + "src/test/webapp";
		tomcat.setBaseDir("./target/tomcat");
		tomcat.setPort(52280);

		// tomcat.addWebapp("/examplewebapp", webRoot);
		// or we could do this for root context:
		tomcat.addWebapp("/", webRoot);
		tomcat.start();
		tomcat.getServer().await();
	}

	private void doConfigureHbase() throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
		logger.info("Configuring hbase");
		HBaseAdmin adminClient = new HBaseAdmin(HBaseConfiguration.create());

		TableName metricTableName = TableName.valueOf(METRIC_TABLE);
		HTableDescriptor metricTableDescriptor = null;
		try {
			metricTableDescriptor = adminClient.getTableDescriptor(metricTableName);
			if (metricTableDescriptor == null) {
				throw new TableNotFoundException();
			}
		} catch (TableNotFoundException tnf) {
			logger.info("Creating metric table: Table => " + METRIC_TABLE + " Col family => " + METRIC_TABLE_COLUMN_FAMILY);
			metricTableDescriptor = new HTableDescriptor(metricTableName);
			HColumnDescriptor columnDescriptor = new HColumnDescriptor(METRIC_TABLE_COLUMN_FAMILY);
			metricTableDescriptor.addFamily(columnDescriptor);
			adminClient.createTable(metricTableDescriptor);
		} finally {
			adminClient.close();
		}
	}
}