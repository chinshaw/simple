package com.artisan.orchestrator.rest.client;

//
//public class OperationResourceClientTest extends GuiceJerseyTest {
//
//	public static class TestModule extends JerseyModule {
//
//		@Override
//		protected void configure(Binder binder) {
//			binder.install(new IOCOrchestratorWebModule());
//		}
//	}
//
//	private static final Logger logger = Logger.getLogger(OperationResourceClientTest.class.getName());
//	
//	private ArtisanClient client;
//	
//	@Inject
//	EventBus eventBus;
//
//	public OperationResourceClientTest() {
//		super(TestModule.class);
//	}
//
//	@Before
//	public void before() {
//		client = new ArtisanClient(Client.create(ArtisanClient.clientConfig), getBaseURI().toString());
//		client.enableDebug();
//	}
//
//
//	@Test
//	public void textExecuteWithPlot() throws IOException, InterruptedException, HadoopJobException {
//		final CountDownLatch latch = new CountDownLatch(1);
//
//		final HadoopOperationJobConfiguration.Builder confBuilder = new HadoopOperationJobConfiguration.Builder();
//	//	confBuilder.setReducerOperation(createTestOperation());
//
//		IOperationExecutionService opExec = client.createExecutionService();
//
//		IHadoopJobConfiguration configuration = confBuilder.build();
//		final String jobId = opExec.execute(configuration);
//
//		assert (jobId != null);
//		logger.info("submitted job => " + jobId);
//		
//		final Object callback = new Object() {
//
//			@Subscribe
//			public void onJobCompletion(JobCompletionEvent event) {
//				if (event.getJobId().equals(jobId)) {
//					// Check that our job id's match
//					assert (event.getJobId().equals(jobId));
//					logger.info("callback: jobid => " + event.getJobId());
//					// unlock
//					latch.countDown();
//				}
//			}
//		};
//
//		eventBus.register(callback);
//
//		// Start max wait if we get to two minutes, for readability I use
//		// TimeUnit
//		latch.await(30, TimeUnit.SECONDS);
//
//		assert (latch.getCount() != 1);
//		assertNotNull(jobId);
//	}
//
//	
//	/**
//	 * Create a default operation that runs the bollinger script. It uses
//	 * the test id of 123 and an operation id of 123. It also has 1 output of instrument.png 
//	 * for a single plot based on the metrics of the R script.
//	 * 
//	 * @return
//	 * @throws IOException
//	 */
//	/*
//	public static final AnalyticsOperation createTestOperation() throws IOException {
//		String script = ClasspathUtils.getScriptCode("/com/simple/engine/rscripts/BollingerScript.R");
//		RAnalyticsOperation operation = new RAnalyticsOperation("runTestScript");
//		operation.setId(123l);
//		AnalyticsOperationOutput out = new AnalyticsOperationOutput("/tmp/instrument.png", AnalyticsOperationOutput.Type.BINARY);
//		out.setId(123l);
//		operation.addOutput(out);
//		// operation.addOutput(new AnalyticsOperationOutput("y", Type.TEXT));
//		operation.setCode(script);
//
//		return operation;
//	}
//	*/
//}

