package com.artisan.orchestrator.rest.client;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import com.artisan.utils.ClasspathUtils;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Binder;
import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.AnalyticsOperationOutput;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.orchestrator.IOCOrchestratorWebModule;
import com.simple.orchestrator.api.event.JobCompletionEvent;
import com.simple.orchestrator.api.exception.HadoopJobException;
import com.simple.orchestrator.api.rest.HadoopOperationJobConfiguration;
import com.simple.orchestrator.api.service.IOperationExecutionService;
import com.simple.orchestrator.service.web.WebContextInjectorListener;
import com.simple.orchestrator.service.web.rest.MetricResource;
import com.simple.orchestrator.service.web.rest.OperationExecutionResource;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.WebAppDescriptor;

public class TestOperationExecutionService extends GuiceJerseyTest {

	public static class MyModule extends JerseyModule {

		@Override
		protected void configure(Binder binder) {
			binder.install(new IOCOrchestratorWebModule());
		//	binder.bind(OperationExecutionResource.class);
		//	binder.bind(MetricResource.class);
		}

	}

	
	public static final String RESOURCE_PACKAGES = "com.simple.orchestrator.service.web.rest";

	private static final Logger logger = Logger.getLogger(TestOperationExecutionService.class.getName());

	private ArtisanClient client;

	public TestOperationExecutionService() {
		super(MyModule.class);

	}

	@Before
	public void before() {
		client = new ArtisanClient(client(), getBaseURI().toString());
	}

	protected AppDescriptor configure() {
		return new WebAppDescriptor.Builder().servletClass(GuiceContainer.class).contextListenerClass(WebContextInjectorListener.class)
				.initParam(PackagesResourceConfig.PROPERTY_PACKAGES, RESOURCE_PACKAGES).build();
	} 

	@Test
	public void testFull() throws IOException, InterruptedException, HadoopJobException {
		logger.info("start testExecute");
		final CountDownLatch latch = new CountDownLatch(1);

		final HadoopOperationJobConfiguration.Builder confBuilder = new HadoopOperationJobConfiguration.Builder();
		confBuilder.setAnalyticsOperation(createTestOperation());

		IOperationExecutionService opExec = client.createExecutionService();

		final String jobId = opExec.execute(confBuilder.build());

		// final String jobId =
		// resource.path("execute").type(MediaType.APPLICATION_JSON).entity(confBuilder.build()).post(String.class);
		logger.info("submitted job => " + jobId);
		assert (jobId != null);
		logger.finest("testFull() : jobid => " + jobId);

		final Object callback = new Object() {

			@Subscribe
			public void onJobCompletion(JobCompletionEvent event) {
				if (event.getJobId().equals(jobId)) {
					// Check that our job id's match
					assert (event.getJobId().equals(jobId));
					logger.info("callback: jobid => " + event.getJobId());
					// unlock
					latch.countDown();
				}
			}
		};

		// eventBus.register(callback);

		// Start max wait if we get to two minutes, for readability I use
		// TimeUnit
		latch.await(120, TimeUnit.SECONDS);

		assert (latch.getCount() != 1);
		assertNotNull(jobId);

		logger.info("end testExecute");
	}

	public static final AnalyticsOperation createTestOperation() throws IOException {
		String script = ClasspathUtils.getScriptCode("/com/simple/engine/rscripts/BollingerScript.R");
		RAnalyticsOperation operation = new RAnalyticsOperation("runTestScript");
		operation.setId(99999l);
		AnalyticsOperationOutput out = new AnalyticsOperationOutput("/tmp/instrument.png", AnalyticsOperationOutput.Type.BINARY);
		out.setId(123l);
		operation.addOutput(out);
		// operation.addOutput(new AnalyticsOperationOutput("y", Type.TEXT));
		operation.setCode(script);

		return operation;
	}
}
