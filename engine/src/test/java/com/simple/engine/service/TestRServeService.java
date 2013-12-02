package com.simple.engine.service;

import java.io.IOException;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.simple.domain.AnalyticsOperationOutput;
import com.simple.domain.RAnalyticsOperation;
import com.simple.engine.IOCEngineModule;
import com.simple.engine.utils.ScriptUtils;
import com.simple.original.api.analytics.IAnalyticsOperationOutput;

public class TestRServeService {

	private static final Injector injector = Guice.createInjector(new IOCEngineModule());
	
	@Inject
	private AnalyticsOperationProvider provider;
	
	public TestRServeService() {
		injector.injectMembers(this);
	}
	
	@Test
	public void runHelloWorld() throws AnalyticsOperationException {
		RAnalyticsOperation operation = new RAnalyticsOperation("runTestScript");
		operation.setCode("print ( \"Hello, world!\", quote = FALSE )");
		provider.execute(operation, null);
	}
	
	
	@Test
	public void testInteger() throws AnalyticsOperationException {
		RAnalyticsOperation operation = new RAnalyticsOperation("runTestScript");
		operation.setCode("print ( \"Hello, world!\", quote = FALSE )");
		provider.execute(operation, null);
	}
	
	@Test
	public void testPlotting() throws AnalyticsOperationException, IOException {
		String script = ScriptUtils.getScriptCode("/com/simple/engine/rscripts/BollingerScript.R");
		RAnalyticsOperation operation = new RAnalyticsOperation("runTestScript");
		operation.addOutput(new AnalyticsOperationOutput("metricPlot",IAnalyticsOperationOutput.Type.GRAPHIC));
		operation.setCode(script);
		
	}
	
	@Test
	public void testParseError() {
		
	}
}