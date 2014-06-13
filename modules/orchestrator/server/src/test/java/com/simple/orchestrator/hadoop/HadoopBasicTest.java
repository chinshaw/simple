package com.simple.orchestrator.hadoop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.junit.Test;

import com.artisan.utils.ClasspathUtils;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.simple.domain.model.AnalyticsOperationOutput;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.dataprovider.HttpDataProvider;
import com.simple.orchestrator.IOCOrchestratorTestModule;
import com.simple.orchestrator.api.exception.HadoopJobException;
import com.simple.orchestrator.api.rest.HadoopOperationJobConfiguration;
import com.simple.orchestrator.api.rest.HadoopOperationJobConfiguration.Builder;
import com.simple.orchestrator.hadoop.config.ConfigurationException;
import com.simple.orchestrator.hadoop.mrv2.OperationDriver;
import com.simple.orchestrator.service.AnalyticsOperationException;


/**
 * This allows for testing local hadoop taskss.
 * 
 * @author chris
 *
 */
public class HadoopBasicTest {

	private static final Logger logger = Logger.getLogger(HadoopBasicTest.class.getName());
	
	@Inject
	private OperationDriver executor; 
	
	public HadoopBasicTest() {
		Injector injector = Guice.createInjector(new IOCOrchestratorTestModule());
		injector.injectMembers(this);
	}
	
	@Test
	public void testBasic() throws AnalyticsOperationException, ConfigurationException, HadoopJobException {
		RAnalyticsOperation operation = new RAnalyticsOperation("runTestScript");
		operation.setCode("print ( \"Hello, world!\", quote = FALSE )");
		Builder builder = new HadoopOperationJobConfiguration.Builder();
		builder.setAnalyticsOperation(operation);
		executor.execute(builder.build());
	}
	
	@Test
	public void testGraphic() throws IOException, AnalyticsOperationException, ConfigurationException, HadoopJobException {
		logger.info("testGraphic");
		String script = ClasspathUtils.getScriptCode("/com/simple/engine/rscripts/BollingerScript.R");
		RAnalyticsOperation operation = new RAnalyticsOperation("runTestScript");
		operation.addOutput(new AnalyticsOperationOutput("/tmp/instrument.png", AnalyticsOperationOutput.Type.BINARY));
		//operation.addOutput(new AnalyticsOperationOutput("y", Type.TEXT));
		operation.setCode(script);

		Builder builder = new HadoopOperationJobConfiguration.Builder();
		
		HttpDataProvider dp = new HttpDataProvider("http://ichart.finance.yahoo.com/table.csv?s=HPQ&a=00&b=12&c=2013&d=00&e=15&f=2014&g=d&ignore=.csv");
		List<DataProvider> dps = new ArrayList<DataProvider>();
		dps.add(dp);
		
		builder.addDataProvider(dp).setAnalyticsOperation(operation);
		
		executor.execute(builder.build());
	}
	
	@Test
	public void testEventing() {
		
	}
}