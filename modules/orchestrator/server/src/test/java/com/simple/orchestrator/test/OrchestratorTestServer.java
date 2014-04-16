package com.simple.orchestrator.test;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class OrchestratorTestServer {

	private Tomcat tomcat = new Tomcat();

	public void start() throws LifecycleException, InterruptedException, ServletException, IOException {

		String currentDir = new File(".").getCanonicalPath();

		String webRoot = currentDir + File.separatorChar + "src/test/webapp";
		tomcat.setBaseDir("./target/tomcat");
		tomcat.setPort(52280);

		// tomcat.addWebapp("/examplewebapp", webRoot);
		// or we could do this for root context:
		tomcat.addWebapp("/", webRoot);
		tomcat.start();
		//tomcat.getServer().await();
	}

	public void stop() throws LifecycleException {
		tomcat.getServer().stop();
	}

	public static void main(String[] args) throws LifecycleException, InterruptedException, ServletException, IOException {
		OrchestratorTestServer orchestrator = new OrchestratorTestServer();
		orchestrator.start();
	}
}