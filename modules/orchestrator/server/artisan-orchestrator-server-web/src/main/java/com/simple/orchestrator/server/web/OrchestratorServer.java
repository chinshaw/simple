package com.simple.orchestrator.server.web;

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

import com.simple.orchestrator.server.ModuleProperties;

public class OrchestratorServer {

	public static final int DEFAULT_PORT = 52280;
	
	public static final String DEFAULT_HOST = "127.0.0.1";
	
	private static final Logger logger = Logger.getLogger(OrchestratorServer.class.getName());

	private static final ModuleProperties props = ModuleProperties.getInstance();

	private static final String METRIC_TABLE = props.getProperty("com.artisan.orchestrator.hbase.metric.table");

	private static final String METRIC_TABLE_COLUMN_FAMILY = props.getProperty("com.artisan.orchestrator.hbase.metric.colfamily");

	private Tomcat tomcat = new Tomcat();

	private String host;
	
	private int port;
	
	private boolean wait;
	
	
	/**
	 * Construct a default server that will not continue to run when
	 * the process dies. This is really for internal and should not
	 * be used other than test cases.
	 * @param host
	 * @param port
	 */
	private OrchestratorServer(String host, int port) {
		this(host, port, false);
	}
	
	/**
	 * @see #OrchestratorServer(String, int) 
	 * 
	 * This adds an extra argument to make the server wait for connections;
	 * you will need to stop the server with the {@link #stop()} method.
	 * 
	 * @param host
	 * @param port
	 * @param wait
	 */
	public OrchestratorServer(String host, int port, boolean wait) {
		this.host = host;
		this.port = port;
		this.wait = wait;
	}
	
	
	public void start() throws LifecycleException, InterruptedException, ServletException, IOException {
		logger.info("Starting");
		
		doConfigureHbase();
		doStartWebServer();
		
		logger.info("Orchestrator Server: Started");
	}
	

	/**
	 * Call to stop the server.
	 * @throws LifecycleException
	 */
	public void stop() throws LifecycleException {
		tomcat.getServer().stop();
	}

	

	/**
	 * Internal call to start the server.
	 * @throws IOException
	 * @throws ServletException
	 * @throws LifecycleException
	 */
	private void doStartWebServer() throws IOException, ServletException, LifecycleException {
		logger.info("Starting web server");
		String currentDir = new File(".").getCanonicalPath();

		String webRoot = currentDir + File.separatorChar + "src/test/webapp";
		tomcat.setBaseDir("./target/tomcat");
		tomcat.setPort(this.port);

		// tomcat.addWebapp("/examplewebapp", webRoot);
		// or we could do this for root context:
		tomcat.addWebapp("/", webRoot);
		tomcat.start();
		logger.info("Orchestrator Server Started, who rocks!! :)");
		
		if (wait) tomcat.getServer().await();
	}

	/**
	 * This will attempt to configure HBASE in case it is not already configured. 
	 * I will create the default metric table and column family if it does not exist.
	 * 
	 * @throws MasterNotRunningException
	 * @throws ZooKeeperConnectionException
	 * @throws IOException
	 */
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
	
	

	/**
	 * Internal constructor that will start the orchestrator server, this uses
	 * the value DEFAULT_HOST and DEFAULT_PORT for configuration parameters.
	 * @param args
	 * @throws LifecycleException
	 * @throws InterruptedException
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void main(String[] args) throws LifecycleException, InterruptedException, ServletException, IOException {
		OrchestratorServer orchestrator = new OrchestratorServer(DEFAULT_HOST, DEFAULT_PORT);
		orchestrator.start();
	}
	
	public static final OrchestratorServer create(String host, int port, boolean wait) {
		return new OrchestratorServer(host, port, wait);
	}
	
	
}