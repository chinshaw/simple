package com.simple.domain.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.simple.domain.model.AnalyticsOperationOutput;
import com.simple.domain.model.AnalyticsTask;
import com.simple.domain.model.Monitor;
import com.simple.domain.model.Person;
import com.simple.domain.model.Subscription;
import com.simple.domain.model.metric.Metric;
import com.simple.domain.model.metric.MetricNumber;
import com.simple.original.api.analytics.IPerson;
import com.simple.original.api.domain.RecordFecthType;
import com.simple.original.api.domain.SortOrder;
import com.simple.original.api.exceptions.DomainException;
import com.simple.original.api.exceptions.NoSuchUserException;
import com.simple.original.api.exceptions.NotAuthenticatedException;


/**
 * Dao to access AlertTaskMonitor objects in the database.
 * 
 * 
 * @author chinshaw
 * 
 */
public class AnalyticsTaskMonitorDao extends DaoBase<Monitor>
		implements IDaoRequest<Monitor> {

	private static final Logger logger = Logger
			.getLogger(AnalyticsTaskMonitorDao.class.getName());

	public AnalyticsTaskMonitorDao() {
		super(Monitor.class);
	}

	@Override
	public Long save(Monitor alert) throws DomainException {
		logger.info("Calling save on alert definition");
		if (alert.getOwner() == null) {
			assignCurrentPersonToAlertDefinition(alert);
		}
		return super.save(alert);
	}

	/**
	 * 
	 * @param alert
	 * @throws RuntimeException
	 * @throws NoSuchUserException
	 */
	private void assignCurrentPersonToAlertDefinition(Monitor alert)
			throws RuntimeException, NoSuchUserException {
		Person person = getCurrentPerson();

		if (person == null) {
			throw new RuntimeException(
					"Unable to save AlertDefinition, current session id does not map to a valid user");
		}
		// Add the owner to the alert definition.
		alert.setOwner(person);
	}

	public List<AnalyticsOperationOutput> getOperationOutputs(Long taskId) {
		EntityManager em = getEntityManager();

		AnalyticsTask task = em.find(AnalyticsTask.class, taskId);
		return task.getAllOutputs();
	}

	public Monitor saveAndReturn(Monitor alert) {
		return super.saveOrUpdate(alert);
	}

	/**
	 * Delete a task monitor by it's database id. This calls the parent @link
	 * {@link DaoBase#delete(Long)}
	 * 
	 * @param id
	 * @throws NotAuthenticatedException
	 */
	public void deleteId(Long id) throws NotAuthenticatedException {
		super.delete(id);
	}

	@Override
	public Monitor find(Long alertId) {
		return super.find(alertId);
	}

	/**
	 * returns Alert Definition(s) related to Task(s)
	 * 
	 * @param taskIdsToDelete
	 * @return
	 * 
	 */
	public List<Monitor> findAlertsRelatedToTask(Set<Long> taskIds) {
		EntityManager em = getEntityManager();
		TypedQuery<Monitor> query = em.createNamedQuery(
				"AnalyticsTaskMonitor.byTasks", Monitor.class);
		query.setParameter("taskIdList", taskIds);
		List<Monitor> alertDefinitions = new ArrayList<Monitor>();
		alertDefinitions.addAll(query.getResultList());
		logger.info("alertDefinitions.size ->" + alertDefinitions.size());
		return alertDefinitions;
	}

	/**
	 * Returns the associated AlertDefinition for a merticId
	 * 
	 * @param metricId
	 * @return
	 * 
	 *         private List<AnalyticsTaskMonitor>
	 *         findAlertDefinitionByMetricId(Long metricId) {
	 *         logger.entering("findAlertDefinitionByMetricId():",
	 *         " metricId is = " + metricId); List<AnalyticsTaskMonitor>
	 *         matchedAlertDefinitions = new ArrayList<AnalyticsTaskMonitor>();
	 * 
	 *         AlertTaskMonitorDao alertDefinitionDao = new
	 *         AlertTaskMonitorDao(); List<AnalyticsTaskMonitor>
	 *         alertDefinitionList = alertDefinitionDao.listAll();
	 * 
	 *         for (AnalyticsTaskMonitor alertDefinition : alertDefinitionList)
	 *         { AnalyticsOperationOutput value = alertDefinition.getOutput();
	 *         if (value instanceof MetricCollection) { MetricCollection
	 *         metricCollection = ((MetricCollection) value); if
	 *         (metricCollection.getId().longValue() == metricId.longValue()) {
	 *         matchedAlertDefinitions.add(alertDefinition); } } else if (value
	 *         instanceof IMetricDoubleEntity) { IMetricDoubleEntity
	 *         metricNumber = (IMetricDoubleEntity) value; if
	 *         (metricNumber.getId().longValue() == metricId.longValue()) {
	 *         matchedAlertDefinitions.add(alertDefinition); } } else if (value
	 *         instanceof IMetricStringEntity) { IMetricStringEntity
	 *         metricString = (IMetricStringEntity) value; if
	 *         (metricString.getId().longValue() == metricId.longValue()) {
	 *         matchedAlertDefinitions.add(alertDefinition); } } else if (value
	 *         instanceof IMetricPlotEntity) { IMetricPlotEntity
	 *         metricStaticChart = (IMetricPlotEntity) value; if
	 *         (metricStaticChart.getId().longValue() == metricId.longValue()) {
	 *         matchedAlertDefinitions.add(alertDefinition); } } }
	 *         logger.exiting("deleteAlertDefinition():", " metricId is = " +
	 *         metricId); return matchedAlertDefinitions; }
	 */

	/**
	 * This method is used to render client side alert subscriptions. It was
	 * created so that we would not have to add a subscribed field to the
	 * AlertDefinition based on each user.
	 * 
	 * @param start
	 * @param max
	 * @param sortColumn
	 * @param sortOrder
	 * @return
	 */
	public List<Subscription> getSubscriptions(int start, int max,
			RecordFecthType recordType, String searchText, String searchColumn,
			String sortColumn, SortOrder sortOrder) {
		List<Monitor> alerts = new ArrayList<Monitor>();
		List<Subscription> subscriptions = new ArrayList<Subscription>();

		alerts = super.search(start, max, recordType, searchText, searchColumn,
				sortColumn, sortOrder);
		// creates subscription Object from AlertDefinition Object
		IPerson currentUser = getSession().getCurrentPerson();

		for (Monitor alert : alerts) {
			Subscription subscription = new Subscription(alert.getId(),
					alert.getName(), alert.getDescription(), alert
							.getSubscribers().contains(currentUser));
			subscriptions.add(subscription);
		}

		logger.info("Subscriptions size is " + subscriptions.size());
		return subscriptions;
	}

	/**
	 * 
	 * This method subscribe & unsubscribe current user to provided alerts
	 * 
	 * @param alertsToBeSubscribedId
	 * @param alertsToBeUnSubscribedId
	 * @param userId
	 * @throws DomainException 
	 */

	public void saveAlertSubscriptions(List<Long> alertsToBeSubscribedId,
			List<Long> alertsToBeUnSubscribedId) throws DomainException {
		Person currentPerson = getCurrentPerson();
		logger.info("AlertDefinitionDao.saveAlertSubscriptions() - called");
		for (Long alertToBeSubscribedId : alertsToBeSubscribedId) {
			Monitor alertToBeSubscribed = find(alertToBeSubscribedId);
			if (!alertToBeSubscribed.getSubscribers().contains(currentPerson)) {
				alertToBeSubscribed.getSubscribers().add(currentPerson);
				save(alertToBeSubscribed);
			}
		}

		for (Long alertToBeUnSubscribedId : alertsToBeUnSubscribedId) {
			Monitor alertToBeUnSubscribed = find(alertToBeUnSubscribedId);
			if (alertToBeUnSubscribed.getSubscribers().contains(currentPerson)) {
				alertToBeUnSubscribed.getSubscribers().remove(currentPerson);
				save(alertToBeUnSubscribed);
			}
		}
	}

	/**
	 * This method retrieves all the alert definitions associated with the
	 * metrics.
	 * 
	 * @param metrics
	 * @return a list of alertDefinitions
	 */
	@SuppressWarnings("unchecked")
	public List<Monitor> getAlertDefinitions(
			List<MetricNumber> metrics) {
		logger.info("Entering AlertDefinitionDao->getAlertDefinitions():  metrics size is = "
				+ metrics.size());

		EntityManager em = getEntityManager();
		Query query = em.createNamedQuery("findAlertsByMetrics");
		query.setParameter("metrics", metrics);
		List<Monitor> matchedAlertsList = (List<Monitor>) query
				.getResultList();
		logger.info("Exiting AlertDefinitionDao->getAlertDefinitions(): Number of matched alertDefinitions are :"
				+ matchedAlertsList.size());
		return matchedAlertsList;

	}

	/**
	 * This method returns the AlertDefinition entity which is associated with
	 * the given metric name
	 * 
	 * This will never work, it needs to get the task execution and get the
	 * metrics from there since this does not specify which task to use it will
	 * fetch every metric with the same name. This function needs to take an id
	 * of an analytics task execution and use the id to retrieve the correct
	 * exectuion then get the metric from it.
	 * 
	 * @param metricName
	 * @param metrics
	 * @return
	 */
	public List<Monitor> findByMetric(Metric metric) {
		return metric.getOrigin().getMonitors();
	}

	/**
	 * This method is used to subscribe the deleteSelectedAlertDefinitions
	 * 
	 * @param alertDefinitionIds
	 * @param personId
	 */
	public void deleteSelectedAlertDefinitions(Set<Long> alertDefinitionIds)
			throws NotAuthenticatedException {
		if (logger.isLoggable(java.util.logging.Level.FINER)) {
			logger.entering(
					"AlertdefinitionDao-deleteSelectedAlertDefinitions() :",
					"alertDefinitionIds =" + alertDefinitionIds.toString());
		}
		try {
			deleteList(alertDefinitionIds);
		} catch (Exception e) {
			logger.info("[AlertdefinitionDao:deleteSubscriber()]Exception caught"
					+ e.toString());
		}
	}
}
