package com.simple.orchestrator.hadoop.mrv2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.artisan.utils.ClasspathUtils;
import com.google.common.eventbus.Subscribe;
import com.simple.domain.model.AnalyticsOperationOutput;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.dataprovider.HttpDataProvider;
import com.simple.orchestrator.OrchestratorTest;
import com.simple.orchestrator.api.rest.HadoopOperationJobConfiguration;
import com.simple.orchestrator.api.rest.HadoopOperationJobConfiguration.Builder;
import com.simple.orchestrator.server.event.IEventConnector;
import com.simple.orchestrator.server.event.OperationReducerStateChange;
import com.simple.orchestrator.server.hadoop.mrv2.OperationDriver;

public class TestOperationDriverEventing extends OrchestratorTest {
	
	public static Logger logger = Logger
			.getLogger(TestOperationDriverEventing.class.getName());
	
	@Inject
	private OperationDriver executor;

	@Inject
	private IEventConnector eventConnector;

	@Before
	public void before() {
		getInjector().injectMembers(this);
	}
	
	/**
	 * This needs to test that the driver will send a basic operation
	 * started event. If it does this will unlock the latch
	 * and complete the task and move on.
	 */
	@Test
	public void testStartedEvent() {
			logger.info("testGraphic");
			String script = ClasspathUtils
					.getScriptCode("/com/simple/engine/rscripts/BollingerScript.R");
			RAnalyticsOperation operation = new RAnalyticsOperation("runTestScript");
			AnalyticsOperationOutput output = new AnalyticsOperationOutput(
					"/tmp/instrument.png", AnalyticsOperationOutput.Type.BINARY);
			output.setId(1233l);
			operation.addOutput(output);
			// operation.addOutput(new AnalyticsOperationOutput("y", Type.TEXT));
			operation.setCode(script);
			operation.setId(445L);

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

			builder.addDataProvider(dp).setAnalyticsOperation(operation);

			executor.execute(builder.build());
			latch.await(300, TimeUnit.SECONDS);
	}
}
