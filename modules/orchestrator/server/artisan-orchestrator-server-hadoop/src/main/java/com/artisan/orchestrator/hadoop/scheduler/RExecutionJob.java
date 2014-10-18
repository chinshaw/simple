package com.artisan.orchestrator.hadoop.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class RExecutionJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		
	}
//
//	public static final String ANALYTICS_TASK_ID = "analyticsTaskId";
//
//	public static final String INPUTS = "inputs";
//	private static final Logger logger = Logger.getLogger("RExecutionJob");
//
//	// private Thread jobThread = null;
//
//	private final AnalyticsTaskDao taskDao;
//
//	private final AnalyticsTaskExecutionDao executionDao;
//
//	private final AnalyticsTaskService service;
//
//	@Inject
//	public RExecutionJob(AnalyticsTaskDao taskDao,
//			AnalyticsTaskExecutionDao executionDao, AnalyticsTaskService service) {
//		this.taskDao = taskDao;
//		this.executionDao = executionDao;
//		this.service = service;
//	}
//
//	@Override
//	public void execute(JobExecutionContext context)
//			throws JobExecutionException {
//		try {
//			// jobThread = Thread.currentThread();
//			doRunJob(context);
//		} catch (DomainException e) {
//			logger.info("Unable to save task execution in datastore");
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	private void doRunJob(JobExecutionContext context)
//			throws JobExecutionException, DomainException {
//		logger.info("Executing new job");
//
//		AnalyticsTaskExecution execution;
//
//		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
//		try {
//			logger.info("Attempting to get job detail");
//			JobDetail jobDetail = context.getJobDetail();
//			logger.info("Starting task key->" + jobDetail.getKey().toString()
//					+ " detail -> " + jobDetail.getDescription() + " time -> "
//					+ new Date());
//
//			Long taskId = dataMap.getLong(ANALYTICS_TASK_ID);
//
//			if (taskId == null) {
//				throw new JobExecutionException(
//						"Task id was null for this job "
//								+ jobDetail.getKey().toString());
//			}
//
//			AnalyticsTask task = taskDao.find(taskId);
//
//			if (task == null) {
//				throw new JobExecutionException("Task with id " + taskId
//						+ " was not found in the datastore");
//			}
//
//			execution = service.executeAnalyticsTask(task,
//					(List<AnalyticsOperationInput>) dataMap.get(INPUTS), null);
//			executionDao.save(execution);
//
//			logger.info("Finished task key->" + jobDetail.getKey().toString()
//					+ " detail -> " + jobDetail.getDescription() + " time -> "
//					+ new Date());
//			logger.info("Done Checking outputs in task execution");
//		} catch (AnalyticsTaskExecutionException e) {
//			logger.fatal("Unable to execute task ", e);
//			throw new JobExecutionException("Unable to execute script as job",
//					e);
//		}
//	}
//
//	/*
//	 * @Override public void interrupt() throws UnableToInterruptJobException {
//	 * logger.info("Interrupting job " + getClass().getName()); if (jobThread !=
//	 * null) { jobThread.interrupt(); } }
//	 */
}
