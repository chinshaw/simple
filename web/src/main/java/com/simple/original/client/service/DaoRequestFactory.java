package com.simple.original.client.service;

import java.util.List;
import java.util.Set;

import com.google.web.bindery.requestfactory.shared.ExtraTypes;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.google.web.bindery.requestfactory.shared.SkipInterfaceValidation;
import com.simple.api.domain.RecordFecthType;
import com.simple.api.domain.SortOrder;
import com.simple.domain.dao.AnalyticsOperationDao;
import com.simple.domain.dao.AnalyticsTaskDao;
import com.simple.domain.dao.DaoBase;
import com.simple.domain.dao.PersonDao;
import com.simple.domain.model.ui.dashboard.DashboardDao;
import com.simple.original.client.proxy.AnalyticsOperationInputProxy;
import com.simple.original.client.proxy.AnalyticsOperationNameProxy;
import com.simple.original.client.proxy.AnalyticsOperationOutputProxy;
import com.simple.original.client.proxy.AnalyticsOperationProxy;
import com.simple.original.client.proxy.AnalyticsTaskNameProxy;
import com.simple.original.client.proxy.AnalyticsTaskProxy;
import com.simple.original.client.proxy.ApplicationBookmarkProxy;
import com.simple.original.client.proxy.DashboardProxy;
import com.simple.original.client.proxy.DatastoreObjectProxy;
import com.simple.original.client.proxy.JavaAnalyticsOperationProxy;
import com.simple.original.client.proxy.LinkableDashboardProxy;
import com.simple.original.client.proxy.PersonProxy;
import com.simple.original.client.proxy.PreferencesProxy;
import com.simple.original.client.proxy.RAnalyticsOperationProxy;
import com.simple.original.client.proxy.UIComplexInputModelProxy;
import com.simple.original.client.proxy.UIDateInputModelProxy;
import com.simple.original.client.proxy.UIUserInputModelProxy;
import com.simple.original.server.service.InjectingServiceLocator;

public interface DaoRequestFactory extends RequestFactory {

	@SkipInterfaceValidation
	@Service(value = DaoBase.class, locator = InjectingServiceLocator.class)
	public interface DaoRequest<T extends DatastoreObjectProxy> extends PaginationRequest<T>, SearchableRequest<T> {

		Request<T> find(Long id);

		/**
		 * Save the object and return the key of the object.
		 * 
		 * @param obj
		 * @return
		 */
		Request<T> copy(Long id);

		Request<Long> save(T obj);

		Request<T> saveAndReturn(T obj);

		Request<List<T>> listAll();

		Request<Void> delete(Long id);

		Request<Integer> deleteList(Set<Long> idsToDelete);

		Request<List<Long>> fetchIds();

		Request<List<T>> findList(Set<Long> ids);
	}

	/**
	 * Service stub for methods in PersonDao
	 */
	@Service(value = PersonDao.class, locator = InjectingServiceLocator.class)
	public interface PersonRequest extends RequestContext {

		// TODO this needs to go away, we don't want to expose this on client
		// side.
		// It is here to support the preferences but we should just save the
		// preferences.
		// Request<PersonProxy> saveAndReturn(PersonProxy user);

		Request<List<PersonProxy>> listAll();

		Request<PersonProxy> findUser(String email);

		Request<Void> savePreferences(PreferencesProxy preferences);

		Request<PreferencesProxy> getPreferencesForCurrentUser();

		Request<PersonProxy> getCurrentPerson();

		/**
		 * Adds a new bookmark for the currently logged in user.
		 * 
		 * @param bookmark
		 * @return
		 */
		Request<Boolean> addBookmark(ApplicationBookmarkProxy bookmark);

		/**
		 * Remove a bookmark from the current user.
		 * 
		 * @param bookmark
		 * @return
		 */
		Request<Boolean> removeBookmark(ApplicationBookmarkProxy bookmark);

		// Request<Void> saveUserTaskLimit(List<PersonProxy> selectedUsersList,
		// String taskLimitValue);
		Request<List<PersonProxy>> search(int start, int max, RecordFecthType recordType, String searchText, String searchColumn,
				String sortColumn, SortOrder sortOrder);
	}

	@ExtraTypes({ RAnalyticsOperationProxy.class, JavaAnalyticsOperationProxy.class, AnalyticsOperationInputProxy.class,
			AnalyticsOperationOutputProxy.class, UIUserInputModelProxy.class, UIDateInputModelProxy.class, UIComplexInputModelProxy.class, })
	@Service(value = AnalyticsOperationDao.class, locator = InjectingServiceLocator.class)
	public interface AnalyticsOperationRequest extends DaoRequest<AnalyticsOperationProxy> {

		Request<Long> save(AnalyticsOperationProxy operation);

		/**
		 * Fetches all the operations.
		 */
		Request<List<AnalyticsOperationProxy>> listAll();

		Request<List<AnalyticsOperationNameProxy>> listOperationNames();

		Request<AnalyticsOperationProxy> saveAndReturn(AnalyticsOperationProxy operation);

		Request<AnalyticsOperationProxy> find(Long analyticsOperationId);

		Request<Integer> deleteList(Set<Long> idsToDelete);

		/**
		 * Request to retrieve the input for an operation by it's id.
		 * 
		 * @param operationId
		 * @return
		 */
		Request<List<AnalyticsOperationInputProxy>> listInputs(Long operationId);
	}

	/**
	 * Analytics Task request is the accessor to Analytics Task objects in the
	 * datastore.
	 * 
	 * @author chinshaw
	 */
	@ExtraTypes({ RAnalyticsOperationProxy.class, DashboardProxy.class, UIUserInputModelProxy.class, UIDateInputModelProxy.class, UIComplexInputModelProxy.class,
			LinkableDashboardProxy.class })
	@Service(value = AnalyticsTaskDao.class, locator = InjectingServiceLocator.class)
	public interface AnalyticsTaskRequest extends DaoRequest<AnalyticsTaskProxy> {

		/**
		 * This is used to clone a analytics task on the server side. It should
		 * return a copy of the analytics task with all the id's and versions
		 * stripped out. It is not saved into the datastore so would need to be
		 * persisted once it is manipulated. This will do a deep copy so you can
		 * request the script, scirpt.inputs and script.outputs when you request
		 * the object. Those will be cloned also.
		 * 
		 * @param analyticsTaskId
		 *            The key of the analyticsTask to copy.
		 * @return Cloned analytics task object of the analyticsTaskId param.
		 */
		Request<AnalyticsTaskProxy> copy(Long taskId);

		/**
		 * Save a analyticsTask to the datatore
		 */
		@Override
		Request<Long> save(AnalyticsTaskProxy obj);

		/**
		 * This will return all tasks the user currently has access to, in the
		 * case of an administrator it will return all.
		 */
		Request<List<AnalyticsTaskProxy>> listAll();

		/**
		 * This will return just the tasks that are owned by the current user.
		 * 
		 * @return
		 */
		Request<List<AnalyticsTaskProxy>> listPersonal();

		/**
		 * Returns the the names of all tasks only, this should be used all of
		 * the time and not call the listAll function because of the heavy wait
		 * of the listAll function.
		 * 
		 * @return
		 */
		Request<List<AnalyticsTaskNameProxy>> listAllTaskNames();

		/**
		 * Fetches all the name of the tasks owned by the current person, you
		 * should use this over the listPersonal unless absolutely necessary.
		 * 
		 * @return
		 */
		Request<List<AnalyticsTaskNameProxy>> listTaskNamesForCurrentPerson();

		/**
		 * Save the analytics task to the datastore and return the persisted
		 * analytics task back to the caller.
		 */
		@Override
		Request<AnalyticsTaskProxy> saveAndReturn(AnalyticsTaskProxy obj);

		Request<Integer> deleteList(Set<Long> idsToDelete);

		Request<List<AnalyticsTaskProxy>> getAnalyticsTasksForOperationIds(Set<Long> operationIds);

		Request<List<AnalyticsOperationInputProxy>> listAllInputs(Long taskId);

		/**
		 * This method will retrieve all the defined outputs for all the
		 * operations listed in the task chain of a task.
		 * 
		 * @param taskId
		 * @return
		 */
		Request<List<AnalyticsOperationOutputProxy>> listAllOutputs(Long taskId);

		// Request<List<SubscriptionProxy>> getSubscriptions(int start, int max,
		// String sortColumn, SortOrder sortOrder);

		// Request<Void> saveReportSubscriptions(List<Long>
		// reportsToBeSubscribed, List<Long> reportsToBeUnSubscribed);
	}

	@ExtraTypes({ DashboardProxy.class, RAnalyticsOperationProxy.class, LinkableDashboardProxy.class })
	@Service(value = DashboardDao.class, locator = InjectingServiceLocator.class)
	public interface DashboardRequest extends DaoRequest<DashboardProxy> {

		Request<DashboardProxy> getDashboardForDesigner(Long dashboardId);

		@Override
		Request<Long> save(DashboardProxy dashboard);

		Request<DashboardProxy> findDashboardForTask(Long taskId);
	}

	public PersonRequest personRequest();

	public AnalyticsTaskRequest createAnalyticsTaskRequest();

	public DashboardRequest createDashboardRequest();

	public AnalyticsOperationRequest createAnalyticsOperationRequest();

}
