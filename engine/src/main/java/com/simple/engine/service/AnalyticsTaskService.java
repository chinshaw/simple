package com.simple.engine.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.NoResultException;

import com.google.inject.Inject;
import com.simple.domain.dao.AnalyticsTaskDao;
import com.simple.domain.dao.AnalyticsTaskExecutionDao;
import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.AnalyticsTask;
import com.simple.domain.model.AnalyticsTaskExecution;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.metric.Metric;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.original.api.analytics.IAnalyticsTaskExecution;
import com.simple.original.api.analytics.IAnalyticsTaskExecution.TaskCompletionStatus;
import com.simple.original.api.exceptions.AnalyticsTaskException;
import com.simple.original.api.exceptions.DashboardException;
import com.simple.original.api.exceptions.DomainException;

public class AnalyticsTaskService {

	public static final String OperationLoggerInstance = "OperationLogger";

	/**
	 * Operation and task logger logger.
	 */
	private static final Logger logger = Logger.getLogger(OperationLoggerInstance);

	private static final Logger systemLogger = Logger
			.getLogger("AnalyticsService");

	private final AnalyticsTaskDao taskDao;

	private final AnalyticsTaskExecutionDao executionDao;
	
	private final IAnalyticsOperationExecutor provider;

	@Inject
	public AnalyticsTaskService(AnalyticsTaskDao taskDao,
			AnalyticsTaskExecutionDao executionDao, IAnalyticsOperationExecutor provider) {
		this.taskDao = taskDao;
		this.executionDao = executionDao;
		this.provider = provider;
	}

	/**
	 * This is used to get a previous execution.
	 * 
	 * @param analyticsTaskExecutionId
	 *            The id of the previous execution to fetch.
	 * @return AnalyticsTaskExecution or null if it is not found.
	 * @throws AnalyticsTaskException
	 * @throws DashboardException
	 */
	public AnalyticsTaskExecution getPreviousExecution(
			Long analyticsTaskExecutionId) throws AnalyticsTaskException,
			DashboardException {
		AnalyticsTaskExecution taskCompletion = null;
		try {
			systemLogger
					.info("Getting previous execution for taskexecution id "
							+ analyticsTaskExecutionId);

			taskCompletion = executionDao.find(analyticsTaskExecutionId);

		} catch (NoResultException e) {
			// We didn't get a result so let's run the task and return those
			// results.
			logger.info("Analytics Task was null so we will run the task");
		}
		return taskCompletion;
	}

	/**
	 * Implementation that will return the latest {@link AnalyticsTaskExecution}
	 * . This method may actually run the task and return the results form that
	 * if the task execution was not found.
	 * 
	 * @param taskId
	 *            The id of the task to get last execution.
	 * @return AnalyticsTaskExecution, if the last execution was not found in
	 *         the analytics task then the task will be run and that execution
	 *         will be returned.
	 * @throws AnalyticsTaskException
	 *             If there is an error returned from executing the task.
	 * @throws AnalyticsTaskExecutionException
	 * @throws DashboardException
	 * @throws DomainException 
	 * @throws VirtualFactoryRServiceException
	 *             If there is an error returned while executing the task.
	 */
	public AnalyticsTaskExecution getLatestTaskCompletion(Long taskId)
			throws AnalyticsTaskException, AnalyticsTaskExecutionException,
			DashboardException, DomainException {

		AnalyticsTaskExecution taskCompletion = null;

		try {
			taskCompletion = taskDao.getLastAnalyticsTaskExecution(taskId);
		} catch (NoResultException e) {
			// Do nothing we will return null to client.
			taskCompletion = executeInteractive(taskId, null, null);
		}

		return taskCompletion;
	}

	/**
	 * This function will call the correct analytic's task provider, execute the
	 * task and store the outputs in the datastore. This function will store all
	 * outputs it finds from the provider and you will be in charge or asking
	 * for what you want when fetching report outputs.
	 * 
	 * If an {@link AnalyticsTaskExecution} exception is caught we will try to
	 * make it more web friendly and write the job log out to the filesystem
	 * then simply add the name of the log file.
	 * 
	 * @param analyticsTaskId
	 *            The analytics task id
	 * @param inputs
	 * @return
	 * @throws AnalyticsTaskException
	 * @throws AnalyticsTaskExecutionException
	 * @throws DashboardException
	 * @throws DomainException
	 * @throws IOException
	 * @throws VirtualFactoryRServiceException
	 */
	public AnalyticsTaskExecution executeInteractive(Long analyticsTaskId,
			List<AnalyticsOperationInput> userInputs,
			List<DataProvider> dataProviders)
			throws AnalyticsTaskExecutionException, DashboardException,
			DomainException {

		AnalyticsTask task = taskDao.find(analyticsTaskId);

		if (task == null) {
			throw new AnalyticsTaskExecutionException(
					"Selected task with "
							+ analyticsTaskId
							+ " was not found in the datastore,"
							+ " this may be a cause by an invalid id in the server.properties file for one of the global dashboards");
		}

		// If the user inputs are null let's go get the actual inputs from the
		// operations and assign them as the default inputs. The task will use
		// the default
		// value as it's input.
		if (userInputs == null) {
			userInputs = task.getAllInputs();
		}

		AnalyticsTaskExecution execution = null;

		try {
			execution = executeAnalyticsTask(task, userInputs, dataProviders);
		} catch (AnalyticsTaskExecutionException e) {
			execution = e.getTaskExecution();
			// Notice this may be obscured by the finally block if there
			// is an IOExceptin thrown when trying to save.
			throw e;
		} finally {
			if (execution != null) {
				execution.setInteractive(true);
				executionDao.save(execution);
			}
		}

		return execution;
	}

	/**
	 * Adds the ability to execute an analytics task by it's id.
	 * 
	 * @param taskId
	 * @return
	 * @throws AnalyticsTaskException
	 * @throws DashboardException
	 */
	public AnalyticsTaskExecution executeAnalyticsTask(Long taskId)
			throws AnalyticsTaskExecutionException, DashboardException {
		if (taskId == null) {
			throw new RuntimeException("Task id is a required parameter");
		}
		AnalyticsTask task = taskDao.find(taskId);
		AnalyticsTaskExecution execution = executeAnalyticsTask(task,
				task.getAllInputs(), task.getDataProviders());

		return execution;
	}

	/**
	 * THis is going to be private soon, it is only here for some basic unit
	 * tests
	 * 
	 * @param task
	 * @param userInputs
	 * @param dataProviders
	 * @return
	 * @throws AnalyticsTaskException
	 * @throws AnalyticsTaskExecutionException
	 */
	public AnalyticsTaskExecution executeAnalyticsTask(AnalyticsTask task,
			List<AnalyticsOperationInput> inputs,
			List<DataProvider> dataProviders)
			throws AnalyticsTaskExecutionException {
		if (task == null) {
			throw new AnalyticsTaskExecutionException(
					"Nothing to do here, task was null");
		}

		OperationLogHandler operationLogHandler = null;
		AnalyticsTaskExecution taskExecution = new AnalyticsTaskExecution(task);
		List<AnalyticsOperation> analyticsOperations = task.getOperations();
		taskExecution.setStartTime(new Date());

		try {

			logger.fine("Number of operations is " + analyticsOperations.size());
			for (AnalyticsOperation analyticsOperation : analyticsOperations) {
				if (analyticsOperation instanceof RAnalyticsOperation) {
					logger.fine("Executing " + analyticsOperation.getName());
					
					try {
						Map<Long, Metric> outputs = provider.execute("joe user",
								inputs, analyticsOperation,
								task.getDataProviders());

						if (provider.isOperationSuccessful()) {
							taskExecution.getExecutionMetrics().addAll(
									outputs.values());
						} else {
							taskExecution
									.setCompletionStatus(IAnalyticsTaskExecution.TaskCompletionStatus.FAILED);
						}

					} catch (AnalyticsOperationException e) {
						taskExecution.setExecutionLog(operationLogHandler.dumpLog());
						logger.log(Level.SEVERE, "R AnalyticsException occurred ", e);
						systemLogger.log(Level.SEVERE, "Error occurred", e);
						throw new AnalyticsTaskExecutionException(
								taskExecution, e);
					}

					logger.info("Task Completed");
				}
			}
		} finally {
			if (provider != null) {
				provider.close();
			}
				
		}

		// Set the end time now that all the operations are finished.
		taskExecution.setCompletionTime(new Date());
		taskExecution.setCompletionStatus(TaskCompletionStatus.SUCCESS);

		return taskExecution;
	}




	/**
	 * Get the logger the logger that is used for analytics service logging.
	 * 
	 * @return
	 */
	public static Logger getLogger() {
		return logger;
	}
}
