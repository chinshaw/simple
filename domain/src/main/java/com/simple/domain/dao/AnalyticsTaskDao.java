package com.simple.domain.dao;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.inject.Inject;
import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.AnalyticsOperationOutput;
import com.simple.domain.model.AnalyticsTask;
import com.simple.domain.model.AnalyticsTaskExecution;
import com.simple.domain.model.AnalyticsTaskName;
import com.simple.domain.model.ChangeLog;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.domain.model.RDataProvider;
import com.simple.domain.model.SqlDataProvider;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.metric.MetricCollection;
import com.simple.domain.model.metric.MetricPlot;
import com.simple.domain.model.metric.MetricString;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.domain.model.ui.dashboard.Dashboard;
import com.simple.domain.model.ui.dashboard.Widget;
import com.simple.original.api.analytics.IMetricDouble;
import com.simple.original.api.analytics.IMetricString;
import com.simple.original.api.exceptions.DuplicateTaskException;
import com.simple.original.api.exceptions.SimpleException;

/**
 * 
 * @author chinshaw
 * 
 */
public class AnalyticsTaskDao extends DaoBase<AnalyticsTask> {

	/**
	 * This is a MetaModel that can be used for the AnalyticsTask so that you
	 * can reference classes by attribute.
	 * 
	 * @author chinshaw
	 */
	@StaticMetamodel(AnalyticsTask.class)
	public static class AnalyticsTask_ {
		public static volatile SingularAttribute<AnalyticsTask, Long> id;
		public static volatile SingularAttribute<AnalyticsTask, String> name;
		public static volatile SingularAttribute<AnalyticsTask, String> description;
	}

	/**
	 * This is a wrapper class used for exporting the analytics tasks to JaxB.
	 * We needed a root element to persist the objects so that we weren't using
	 * the List object which does not natively support jaxb.
	 * 
	 * @author chinshaw
	 */
	@XmlRootElement
	public static class AnalyticsTasks {
		protected List<AnalyticsTask> list = new ArrayList<AnalyticsTask>();

		public AnalyticsTasks() {
		}

		public AnalyticsTasks(List<AnalyticsTask> list) {
			this.list = list;
		}

		@XmlElement(name = "analyticsTasks")
		public List<AnalyticsTask> getList() {
			return list;
		}
	}

	/**
	 * Single class logger.
	 */
	private static final Logger logger = Logger
			.getLogger(AnalyticsTaskDao.class.getName());

	@Inject
	private AnalyticsOperationDao operationDao;

	
	public AnalyticsTaskDao() {
		super(AnalyticsTask.class);
	}

	/**
	 * Find an analyticsTask by id
	 * 
	 * @param id
	 *            The primary key for the task.
	 * @return The analytics task found or null.
	 */
	public AnalyticsTask find(Long id) {
		return super.find(id);
	}

	/**
	 * This function will first check to see if the script owner is null and if
	 * so it will add the current logged in user as the owner. This will only
	 * happen when a analytics task is created.
	 * 
	 * @param analyticsTask
	 * @throws DuplicateTaskException
	 * @throws SimpleException
	 *             If current session is not valid
	 */
	@Override
	public Long save(AnalyticsTask analyticsTask) throws DuplicateTaskException {
		return this.saveAndReturn(analyticsTask).getId();
	}

	/**
	 * TODO Bandaid to get the dashbaord to be cloned, hopefully can get this
	 * working cleaner from the the ui. Going to set this to be deprecated and
	 * try to come up with a better solution.
	 * 
	 * @param analyticsTask
	 * @param dashboardId
	 * @return
	 * @throws CloneNotSupportedException
	 * @deprecated
	 * 
	 *             public Long saveCopy(AnalyticsTask analyticsTask, boolean
	 *             cloneDashboard) throws CloneNotSupportedException {
	 *             DashboardDao dao = new DashboardDao();
	 * 
	 *             if (cloneDashboard) { Dashboard toClone =
	 *             dao.find(analyticsTask.getDashboard().getId()); Dashboard
	 *             dashboard = toClone.clone();
	 * 
	 *             analyticsTask.setDashboard(dashboard);
	 *             dashboard.setAnalyticsTask(analyticsTask); }
	 * 
	 *             return save(analyticsTask); }
	 */

	/**
	 * This returns the same AnalyticsTask that was saved.
	 * 
	 * @throws DuplicateTaskException
	 */
	public AnalyticsTask saveAndReturn(AnalyticsTask analyticsTask)
			throws DuplicateTaskException {

		if (analyticsTask.getOwner() == null) {
			analyticsTask.setOwner(getCurrentPerson());
		}
		
		analyticsTask.addChangeLog(new ChangeLog(getCurrentPerson()));
		
		analyticsTask = super.saveOrUpdate(analyticsTask);
		return analyticsTask;
	}

	/**
	 * This function will retrieve only the tasks the user has access to
	 * 
	 */
	public List<AnalyticsTask> listAll() {
		TypedQuery<AnalyticsTask> query = null;

		query = getEntityManager().createNamedQuery("AnalyticsTask.all",
				AnalyticsTask.class);

		return query.getResultList();
	}

	/**
	 * This function will retrieve only the tasks owned by the current user.
	 * 
	 */
	public List<AnalyticsTask> listPersonal() {
		Long userId = getSession().getCurrentPerson().getId();

		TypedQuery<AnalyticsTask> query = getEntityManager().createNamedQuery(
				"AnalyticsTask.byOwner", AnalyticsTask.class);
		query.setParameter("owner", userId);

		return query.getResultList();
	}

	/**
	 * Use this whenever possible!! This will return the complete list of tasks
	 * available.
	 * 
	 * @return
	 */
	public List<AnalyticsTaskName> listAllTaskNames() {
		List<AnalyticsTaskName> taskNames = new ArrayList<AnalyticsTaskName>();

		List<AnalyticsTask> tasks = listAll();
		for (AnalyticsTask task : tasks) {
			taskNames.add(new AnalyticsTaskName(task.getId(), task.getName()));
		}
		return taskNames;
	}

	/**
	 * Simpler for the ui will only return the names of the tasks along with
	 * their ids that belong to the current user.
	 * 
	 * @return
	 */
	public List<AnalyticsTaskName> listTaskNamesForCurrentPerson() {
		List<AnalyticsTaskName> taskNames = new ArrayList<AnalyticsTaskName>();

		List<AnalyticsTask> tasks = listAll();
		for (AnalyticsTask task : tasks) {
			taskNames.add(new AnalyticsTaskName(task.getId(), task.getName()));
		}
		return taskNames;
	}

	/**
	 * This will return the clone of a analytics task. The analytics task must
	 * be detached or it will keep the caching data about the analytics task.
	 * 
	 * @param analyticsTaskId
	 * @return
	 * @throws SimpleException
	 */
	public AnalyticsTask copy(Long analyticsTaskId)
			throws SimpleException {
		if (analyticsTaskId == null) {
			throw new IllegalArgumentException("analyticstaskId cannot be null");
		}
		logger.fine("cloneAnalyticsTask ->id :" + analyticsTaskId);

		AnalyticsTask toClone = find(analyticsTaskId);
		if (toClone == null) {
			throw new IllegalArgumentException("task with id "
					+ analyticsTaskId + " was not found in the datastore");
		}

		AnalyticsTask clone = null;

		try {
			clone = toClone.clone();
			clone.setOwner(getCurrentPerson());
			clone.setName(clone.getName() + "_clone");

			clone.addChangeLog(new ChangeLog(getCurrentPerson(),
					"Cloned from task " + analyticsTaskId));

			clone = saveAndReturn(clone);
		} catch (CloneNotSupportedException e) {
			logger.severe("Unable to clone analytics task object : "
					+ e.getMessage());
			throw new SimpleException(
					"Unable to clone analytics task object", e);
		}

		return clone;
	}

	/**
	 * This method is used to get list of Analytics tasks
	 * 
	 * @param start
	 * @param max
	 * @return
	 */
	public List<AnalyticsTask> find(int start, int max) {

		TypedQuery<AnalyticsTask> query = getEntityManager().createQuery(
				"SELECT task FROM  " + AnalyticsTask.class.getName()
						+ " task ORDER BY task.id ASC", AnalyticsTask.class);
		query.setFirstResult(start);
		query.setMaxResults(max);
		List<AnalyticsTask> resultList = (List<AnalyticsTask>) query
				.getResultList();
		return resultList;

	}

	/**
	 * Used to export analytics tasks from the database. This will also export
	 * the associated analytics operations and dashboards that were created to
	 * support this analytics task.
	 * 
	 * @return The xml string created using JAXB
	 * @throws JAXBException
	 */
	public String exportAnalyticsTasks() throws JAXBException {
		StringWriter buffer = new StringWriter();
		JAXBContext context = JAXBContext.newInstance(AnalyticsTask.class,
				AnalyticsTasks.class, RAnalyticsOperation.class,
				DataProvider.class, SqlDataProvider.class, RDataProvider.class,
				Dashboard.class, Widget.class, MetricPlot.class,
				IMetricDouble.class, IMetricString.class, MetricString.class,
				MetricCollection.class);

		Marshaller marshaller = context.createMarshaller();

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

		List<AnalyticsTask> attached = null;

		TypedQuery<AnalyticsTask> query = getEntityManager().createQuery(
				"SELECT task FROM  " + AnalyticsTask.class.getName() + " task "
						+ "left join fetch task.analyticsOperations "
						+ "left join fetch task.dataProviders",
				AnalyticsTask.class);

		attached = query.getResultList();

		AnalyticsTasks tasks = new AnalyticsTasks();
		for (AnalyticsTask task : attached) {
			tasks.getList().add(task);
		}
		marshaller.marshal(tasks, buffer);
		return buffer.toString();
	}

	/**
	 * Used to import scripts from the JAXB exported tasks
	 * 
	 * @see exportScripts
	 * @param inputStream
	 * @throws JAXBException
	 */
	public void importAnalyticsTasks(InputStream inputStream)
			throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(AnalyticsTask.class,
				AnalyticsTasks.class, RAnalyticsOperation.class,
				DataProvider.class, SqlDataProvider.class, RDataProvider.class,
				Dashboard.class, Widget.class, MetricPlot.class,
				IMetricDouble.class, IMetricString.class, MetricString.class,
				MetricCollection.class);

		Unmarshaller unmarshaller = context.createUnmarshaller();

		AnalyticsTasks analyticsTasks = null;
		try {
			analyticsTasks = (AnalyticsTasks) unmarshaller
					.unmarshal(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}

		EntityManager em = getEntityManager();
		EntityTransaction tx = null;

		try {

			for (AnalyticsTask analyticsTask : analyticsTasks.getList()) {
				// Want to continue on after.
				try {
					tx = em.getTransaction();
					tx.begin();
					em.persist(analyticsTask);
					tx.commit();
				} catch (ConstraintViolationException e) {
					Set<ConstraintViolation<?>> violations = e
							.getConstraintViolations();

					for (ConstraintViolation<?> v : violations) {
						System.out.println(" Violation was " + v.getMessage());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	/*
	 * public void addAnalyticsTaskExecution(AnalyticsTask analyticsTask,
	 * AnalyticsTaskExecution analyticsTaskExecution) { EntityManager em = null;
	 * 
	 * em = getEntityManager();
	 * 
	 * AnalyticsTask attached = em.merge(analyticsTask);
	 * attached.getPreviousExecutions().add(analyticsTaskExecution); }
	 */

	/**
	 * assigns currrent logged in user as owner
	 * 
	 * @param analyticsTask
	 * @throws RuntimeException
	 * @throws NoSuchUserException
	 * 
	 *             private void assignCurrentPersonToAnalyticsTask(AnalyticsTask
	 *             analyticsTask) throws RuntimeException, NoSuchUserException {
	 *             Person person = getSession().getCurrentPerson(); if (person
	 *             == null) { logger.severe(
	 *             "Trying to create a analytics task without a valid id, this appears to be a security breach attempt"
	 *             ); throw new RuntimeException(
	 *             "Unable to save analytics task, current session id does not map to a valid user. This will be logged"
	 *             ); } // Add the owner to the analytics task.
	 *             analyticsTask.setOwner(person); }
	 */

	/**
	 * This will get the last AnalyticsTask Execution for the selected analytics
	 * task. NOTE: The caller is responsible for closing the entity manager.
	 * 
	 * @param analytics
	 *            taskId
	 * @throws IllegalArgumentException
	 *             If the analyticsTaskId is not a valid Long value.
	 * @return
	 */
	public AnalyticsTaskExecution getLastAnalyticsTaskExecution(Long taskId)
			throws NoResultException {
		if (taskId == null) {
			throw new IllegalArgumentException(
					"An invalid analytics task id was supplied -> " + taskId);
		}

		TypedQuery<AnalyticsTaskExecution> query = getEntityManager()
				.createQuery(
						"SELECT execution FROM "
								+ AnalyticsTaskExecution.class.getName()
								+ " as execution where execution.analyticsTask.id = (:analyticsTaskId) order by execution.id DESC",
						AnalyticsTaskExecution.class);

		query.setParameter("analyticsTaskId", taskId);
		query.setMaxResults(1);
		return query.getSingleResult();
	}

	/**
	 * Retrieves all the inputs for a specific task. It iterates over all the
	 * operations and adds their inputs to the returned list.
	 * 
	 * @param taskId
	 *            The id of the analytics task to retrieve inputs for.
	 * @return
	 */
	public List<AnalyticsOperationInput> listAllInputs(Long taskId) {
		List<AnalyticsOperationInput> inputs = new ArrayList<AnalyticsOperationInput>();
		AnalyticsTask task = find(taskId);
		if (task.getOperations() != null) {
			for (AnalyticsOperation operation : task.getOperations()) {
				inputs.addAll(operation.getInputs());
			}
		}
		return inputs;
	}

	/**
	 * Retrieves all the outputs for a specific task. It will iterate over the
	 * operations and add all their defined outputs to the returned list.
	 * 
	 * @param taskId
	 *            The id of the task to retrieve outputs for.
	 * @return
	 */
	public List<AnalyticsOperationOutput> listAllOutputs(Long taskId) {
		List<AnalyticsOperationOutput> outputs = new ArrayList<AnalyticsOperationOutput>();
		AnalyticsTask task = find(taskId);
		for (AnalyticsOperation operation : task.getOperations()) {
			outputs.addAll(operation.getOutputs());
		}

		return outputs;

	}

	/**
	 * This method is used to retrieve all the tasks which are associated with
	 * the analyticsOperationIds
	 * 
	 * @param analyticsOperationIds
	 *            Ids on which a search need to be perform
	 * @return list of matched analytics task
	 */
	public List<AnalyticsTask> getAnalyticsTasksForOperationIds(
			Set<Long> operationIds) {

		List<AnalyticsOperation> operations = operationDao
				.findList(operationIds);

		TypedQuery<AnalyticsTask> query = getEntityManager().createNamedQuery(
				"AnalyticsTask.tasksContainingOperations", AnalyticsTask.class);
		query.setParameter("operations", operations);

		return query.getResultList();
	}

	/**
	 * get private tasks count for users
	 * 
	 * @return
	 */
	public Map<Long, Long> getPrivateTasksCount() {
		Query query = getEntityManager().createNamedQuery(
				"usersPrivateTasksCount");
		@SuppressWarnings("unchecked")
		List<Object[]> taskList = query.getResultList();
		Map<Long, Long> map = new HashMap<Long, Long>();
		for (Object[] resultElement : taskList) {
			Long taskid = (Long) resultElement[0];
			Long taskcount = (Long) resultElement[1];
			logger.fine("usersPrivateTasksCount [id] -> " + taskid
					+ "  , [taskcount] -> " + taskcount);
			map.put(taskid, taskcount);
		}
		return map;
	}

	/**
	 * This is used to fetch all the outputs for a specific analytics task.
	 * 
	 * @param taskId
	 * @return
	 */
	public List<AnalyticsOperationOutput> getAllOuputs(Long taskId) {
		AnalyticsTask task = find(taskId);
		return task.getAllOutputs();

	}
}
