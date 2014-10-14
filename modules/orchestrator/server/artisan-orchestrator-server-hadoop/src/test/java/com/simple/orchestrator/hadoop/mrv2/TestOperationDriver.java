package com.simple.orchestrator.hadoop.mrv2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import com.artisan.utils.ClasspathUtils;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.simple.domain.model.AnalyticsOperationOutput;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.dataprovider.HttpDataProvider;
import com.simple.orchestrator.IOCOrchestratorTestModule;
import com.simple.orchestrator.OrchestratorTest;
import com.simple.orchestrator.api.exception.HadoopJobException;
import com.simple.orchestrator.api.hadoop.operation.RHadoopOperation;
import com.simple.orchestrator.api.rest.HadoopOperationJobConfiguration;
import com.simple.orchestrator.api.rest.HadoopOperationJobConfiguration.Builder;
import com.simple.orchestrator.server.event.IEventConnector;
import com.simple.orchestrator.server.event.OperationReducerStateChange;
import com.simple.orchestrator.server.hadoop.config.ConfigurationException;
import com.simple.orchestrator.server.hadoop.mrv2.OperationDriver;
import com.simple.orchestrator.server.service.AnalyticsOperationException;

/**
 * This allows for testing local hadoop taskss.
 * 
 * @author chris
 * 
 */
public class TestOperationDriver extends OrchestratorTest {

	private static final CountDownLatch latch = new CountDownLatch(1);

	private static final Logger logger = Logger
			.getLogger(TestOperationDriver.class.getName());

	@Inject
	private OperationDriver executor;

	@Inject
	private IEventConnector eventConnector;

	@Before
	public void before() {
		getInjector().injectMembers(this);
	}

	public TestOperationDriver() {
		Injector injector = Guice
				.createInjector(new IOCOrchestratorTestModule());
		injector.injectMembers(this);
	}

	@Test
	public void testBasic() throws AnalyticsOperationException,
			ConfigurationException, HadoopJobException {
		
		RHadoopOperation operation = new RHadoopOperation("runTestScript");
		operation.setCode("print ( \"Hello, world!\", quote = FALSE )");
		Builder builder = new HadoopOperationJobConfiguration.Builder();
		builder.setReducerOperation(operation);
		executor.execute(builder.build());
	}

	@Test
	public void testGraphic() throws IOException, AnalyticsOperationException,
			ConfigurationException, HadoopJobException, InterruptedException {
		logger.info("testGraphic");
		String script = ClasspathUtils
				.getScriptCode("/com/simple/engine/rscripts/BollingerScript.R");
		RHadoopOperation operation = new RHadoopOperation("runTestScript");
		AnalyticsOperationOutput output = new AnalyticsOperationOutput(
				"/tmp/instrument.png", AnalyticsOperationOutput.Type.BINARY);
		output.setId(1233l);
		//operation.addOutput(output);
		// operation.addOutput(new AnalyticsOperationOutput("y", Type.TEXT));
		operation.setCode(script);
		operation.setOperationId(445L);

		logger.info("Connector is in test" + eventConnector);
		eventConnector.subscribe(new Object() {
			@Subscribe
			public void onReducerStateChange(OperationReducerStateChange event) {
				System.out.println("GOTTTT THEE EVENT FOR "
						+ event.getState().name());
			}
		});

		Builder builder = new HadoopOperationJobConfiguration.Builder();

		HttpDataProvider dp = new HttpDataProvider(
				"http://ichart.finance.yahoo.com/table.csv?s=HPQ&a=00&b=12&c=2013&d=00&e=15&f=2014&g=d&ignore=.csv");
		List<DataProvider> dps = new ArrayList<DataProvider>();
		dps.add(dp);

		builder.addDataProvider(dp).setMapperOperation(operation).setReducerOperation(operation);

		executor.execute(builder.build());
		latch.await(300, TimeUnit.SECONDS);
	}

	@Test
	public void testEventing() {

	}
}