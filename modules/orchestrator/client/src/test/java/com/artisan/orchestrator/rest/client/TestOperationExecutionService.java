package com.artisan.orchestrator.rest.client;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.junit.Test;

import com.artisan.utils.ClasspathUtils;
import com.google.common.eventbus.Subscribe;
import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.AnalyticsOperationOutput;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.orchestrator.api.event.JobCompletionEvent;
import com.simple.orchestrator.api.exception.HadoopJobException;
import com.simple.orchestrator.api.rest.HadoopOperationJobConfiguration;
import com.simple.orchestrator.api.service.IOperationExecutionService;

public class TestOperationExecutionService {
	
	private static final Logger logger = Logger.getLogger(TestOperationExecutionService.class.getName());
	
	private ArtisanClient client;

	public TestOperationExecutionService() {
		
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

		//eventBus.register(callback);
		
		// Start max wait if we get to two minutes, for readability I use TimeUnit
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
		//operation.addOutput(new AnalyticsOperationOutput("y", Type.TEXT));
		operation.setCode(script);
		
		return operation;
	}
}
