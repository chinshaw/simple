package com.simple.orchestrator.hadoop.mrv2;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import com.artisan.orchestrator.hadoop.hadoop.mrv2.OperationDriver;
import com.artisan.orchestrator.hadoop.job.io.adapter.local.LocalFileOutputCommitter;
import com.artisan.utils.ClasspathUtils;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.simple.orchestrator.IOCOrchestratorTestModule;
import com.simple.orchestrator.OrchestratorTest;
import com.simple.orchestrator.api.conf.HadoopOperationJobConfiguration;
import com.simple.orchestrator.api.conf.IHadoopJobConfiguration;
import com.simple.orchestrator.api.conf.HadoopOperationJobConfiguration.Builder;
import com.simple.orchestrator.api.conf.impl.ConfigurationException;
import com.simple.orchestrator.api.conf.impl.LocalFileAdapterConfiguration;
import com.simple.orchestrator.api.event.IEventConnector;
import com.simple.orchestrator.api.event.OperationReducerStateChange;
import com.simple.orchestrator.api.exception.HadoopJobException;
import com.simple.orchestrator.api.hadoop.operation.ROperation;
import com.simple.orchestrator.api.hadoop.operation.ROperationOutput;

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
	public void testBasic() throws
			ConfigurationException, HadoopJobException {
		
		ROperation operation = new ROperation("runTestScript");
		operation.setCode("print ( \"Hello, world!\", quote = FALSE )");
		Builder builder = new HadoopOperationJobConfiguration.Builder();
		builder.setReducerOperation(operation);
		executor.execute(builder.build());
	}

	@Test
	public void testGraphic() throws IOException,
			ConfigurationException, HadoopJobException, InterruptedException {
		logger.info("testGraphic");

		final ROperation operation = new ROperation("runTestScript");
		final String script = ClasspathUtils.getScriptCode("/com/simple/engine/rscripts/BollingerScript.R");
		
		operation.setCode(script);
		operation.setId(445L);
		
		final ROperationOutput graphic = new ROperationOutput("/tmp/instrument.png", ROperationOutput.Type.BINARY);
		graphic.setId(1233l);
		operation.addOutput(graphic);
		
		eventConnector.subscribe(new Object() {
			@Subscribe
			public void onReducerStateChange(OperationReducerStateChange event) {
				System.out.println("GOTTTT THEE EVENT FOR "
						+ event.getState().name());
			}
		});

		LocalFileAdapterConfiguration outputConfiguration = new LocalFileAdapterConfiguration();
		outputConfiguration.setOutputFileName("/tmp/testGraphic.json");
		
		Builder builder = new HadoopOperationJobConfiguration.Builder();
		builder.setMapperOperation(operation).setReducerOperation(operation).setOutputAdapterConfiguration(outputConfiguration);
		IHadoopJobConfiguration configuration = builder.build();
		
		/*
		HttpDataProvider dp = new HttpDataProvider(
				"http://ichart.finance.yahoo.com/table.csv?s=HPQ&a=00&b=12&c=2013&d=00&e=15&f=2014&g=d&ignore=.csv");
		List<DataProvider> dps = new ArrayList<DataProvider>();
		
		dps.add(dp);
		 */

		executor.executeAndWait(configuration);
	}

	@Test
	public void testEventing() {

	}
}