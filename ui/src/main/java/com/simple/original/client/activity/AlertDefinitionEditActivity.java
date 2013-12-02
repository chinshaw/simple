package com.simple.original.client.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.api.analytics.IPerson;
import com.simple.original.client.place.AlertDefinitionEditPlace;
import com.simple.original.client.place.MonitoringPlace;
import com.simple.original.client.proxy.AnalyticsOperationOutputProxy;
import com.simple.original.client.proxy.AnalyticsTaskMonitorProxy;
import com.simple.original.client.proxy.AnalyticsTaskProxy;
import com.simple.original.client.proxy.PersonProxy;
import com.simple.original.client.service.DaoRequestFactory.AlertDefinitionRequest;
import com.simple.original.client.view.IAlertDefinitionEditView;

/**
 * @author nallaraj Activity class for AlertDefinition Modify/Create screen
 */
public class AlertDefinitionEditActivity extends AbstractActivity<AlertDefinitionEditPlace, IAlertDefinitionEditView> implements IAlertDefinitionEditView.Presenter {

	/*
	 * Logger instance
	 */
	private static final Logger logger = Logger.getLogger(AlertDefinitionEditActivity.class.getName());

	/**
	 * List of unsubscribed users.
	 */
	private ListDataProvider<String> unsubscribedUserList = new ListDataProvider<String>();

	/**
	 * Holds the AlertDefinitionRequest
	 */
	private AlertDefinitionRequest saveContext = null;

	/**
	 * Selection Model for the Users Celllist
	 */
	private MultiSelectionModel<String> unsubscribedUsersSelectionModel = new MultiSelectionModel<String>();

	/**
	 * Selection Model for Subscribers Cell list
	 */
	private MultiSelectionModel<PersonProxy> subscribersListSelectionModel = new MultiSelectionModel<PersonProxy>();

	/**
	 * The current alert definition being edited.
	 */
	private AnalyticsTaskMonitorProxy alertDefinition = null;

	/**
	 * Current Metric in the metrics valuelistbox in case of AlertDefinition
	 * modification
	 */
	private AnalyticsOperationOutputProxy editableOutput = null;

	/**
	 * Constructor of AlertDefinitionEditActivity
	 * 
	 * @param place
	 * @param clientFactory
	 */
	@Inject
	public AlertDefinitionEditActivity(IAlertDefinitionEditView view) {
		super(view);
	}

	@Override
	protected void bindToView() {
		display.setPresenter(this);

		getAnalyticsTasks();
		display.getErrorPanel().clear();
		display.getTaskDescription().setValue("");

		display.getUnsubscribedUserList().setSelectionModel(unsubscribedUsersSelectionModel);
		display.getSubsribersList().setSelectionModel(subscribersListSelectionModel);

		// subscribedUserList.addDataDisplay(display.getSubsribersList());
		unsubscribedUserList.addDataDisplay(display.getUnsubscribedUserList());

		Long idToEdit = place().getAlertDefinitionId();
		editableOutput = display.getTaskMetricsListBox().getValue();

		if (idToEdit == null) {
			display.getSaveButton().setText("Save");
			createAndEdit();
		} else {
			display.getSaveButton().setText("Update");
			findAndEdit(idToEdit);
		}
	}

	/**
	 * Called when an existing Alert Definition has to be Modified
	 * 
	 * @param alertDefinition
	 */
	private void findAndEdit(Long idToEdit) {
		dao().alertDefinitionRequest().find(place().getAlertDefinitionId()).with(AnalyticsTaskMonitorProxy.EDIT_PROPERTIES).fire(new Receiver<AnalyticsTaskMonitorProxy>() {

			@Override
			public void onSuccess(AnalyticsTaskMonitorProxy alertDefinition) {
				// display.setEnabledPublic(false);
				// display.setVisibleAlertSubscriptionPanel(false);
				if (currentPerson().getRoles().contains(IPerson.Role.ADMIN)) {
					display.setVisibleAlertSubscriptionPanel(alertDefinition.isPublic());
				}

				if (alertDefinition.getAnalyticsTask() != null) {
					display.getTaskDescription().setValue(alertDefinition.getAnalyticsTask().getDescription());
				}

				saveContext = dao().alertDefinitionRequest();
				edit(saveContext.edit(alertDefinition));
			}

			public void onFailure(ServerFailure error) {
				display.showError("Unable to retrieve alert details ", error);
			}
		});
	}

	/**
	 * Create a new alert defintion and then edit it.
	 */
	private void createAndEdit() {
		saveContext = dao().alertDefinitionRequest();
		alertDefinition = saveContext.create(AnalyticsTaskMonitorProxy.class);
		alertDefinition.setAlertStatus(true);
		alertDefinition.setSubscribers(new ArrayList<PersonProxy>());
		edit(alertDefinition);
	}

	/**
	 * Called to edit the alert definition.
	 * 
	 * @param context
	 *            The request context that will save the alert definition, also
	 *            used for editing.
	 * @param alertDefinition
	 *            The alert defintion to edit.
	 */
	private void edit(AnalyticsTaskMonitorProxy alertDefinition) {
		if (currentPerson().getRoles().contains(IPerson.Role.ADMIN)) {
			display.setEnabledPublic(true);
			alertDefinition.setPublic(true);
			display.setVisibleAlertSubscriptionPanel(true);
		} else if (currentPerson().getRoles().contains(IPerson.Role.USER)) {
			display.setEnabledPublic(false);
			display.setVisibleAlertSubscriptionPanel(false);
			alertDefinition.setPublic(false);
		}

		saveContext.save(alertDefinition).with(display.getEditorDriver().getPaths());
		display.getEditorDriver().edit(alertDefinition, saveContext);
	}

	/**
	 * Method to get all the existing analytics tasks
	 */
	private void getAnalyticsTasks() {
		dao().createAnalyticsTaskRequest().listAll().fire(new Receiver<List<AnalyticsTaskProxy>>() {

			@Override
			public void onSuccess(List<AnalyticsTaskProxy> analyticsTasks) {

				if (display.getAnalyticsListBox().getValue() != null) {
					for (AnalyticsTaskProxy analyticsTask : analyticsTasks) {
						if (analyticsTask.getId().longValue() == display.getAnalyticsListBox().getValue().getId().longValue()) {
							analyticsTasks.remove(analyticsTask);
							break;
						}
					}
				}

				sortTaskList(analyticsTasks);
				display.setAcceptableTasks(analyticsTasks);
			}

			/**
			 * Error occurred while fetching the list of tasks
			 */
			public void onFailure(ServerFailure error) {
				display.showError("Unable to retrieve Linked Tasks and Description ", error);
			}
		});
	}

	/**
	 * TO remove the Users from Alert Subscription list.
	 */
	@Override
	public void onRemoveUsers() {

		final Set<PersonProxy> selectedUsers = subscribersListSelectionModel.getSelectedSet();
		if (selectedUsers.size() < 1) {
			display.showError("Please select atleast one subsriber to move to the user list");
			return;
		}

		for (PersonProxy person : selectedUsers) {
			unsubscribedUserList.getList().add(person.getEmail());
		}

		display.getSubscribersEditor().getList().removeAll(selectedUsers);
		sortEmailAddresses(unsubscribedUserList.getList());
		subscribersListSelectionModel.clear();
	}

	/**
	 * This method will grab the selected users in the list of ldap users in the
	 * selection panel and fetch them from the database, it will also create the
	 * user if they do not already exist.
	 */
	@Override
	public void onAddUnsubscribedUsers() {

		final Set<String> selectedUsers = unsubscribedUsersSelectionModel.getSelectedSet();
		if (selectedUsers.size() <= 0) {
			display.showError("Please select atleast one user to move to subsribers list");
			return;
		} else {
			display.getErrorPanel().clear();
		}

		dao().personRequest().listAll().fire(new Receiver<List<PersonProxy>>() {

			@Override
			public void onSuccess(List<PersonProxy> users) {
				display.getSubscribersEditor().getList().addAll(users);

				unsubscribedUserList.getList().removeAll(selectedUsers);
				unsubscribedUsersSelectionModel.clear();
			}
		});

	}

	/**
	 * Called when Save Button is Clicked.
	 */
	@Override
	public void onSave() {
		display.setEnabledSaveButton(false);
		display.setEnabledCancelButton(false);
		display.getErrorPanel().clear();
		RequestFactoryEditorDriver<AnalyticsTaskMonitorProxy, ?> driver = display.getEditorDriver();
		if (place().getAlertDefinitionId() == null && !display.getIsPublicValue()) {
			display.getSubscribersEditor().getList().clear();
		}

		AlertDefinitionRequest request = (AlertDefinitionRequest) driver.flush();
		if (driver.hasErrors()) {
			List<EditorError> errors = driver.getErrors();
			for (EditorError error : errors) {
				logger.info("Errors occurred in AlertDefinitionAddActivity editor" + error.getMessage());
			}
			return;
		}

		request.fire(new Receiver<Void>() {

			public void onFailure(ServerFailure error) {
				display.setEnabledSaveButton(true);
				display.setEnabledCancelButton(true);
				display.showError(error.getMessage());
				logger.info("error occured" + error.getMessage());
				super.onFailure(error);
			}

			@Override
			public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
				display.setEnabledSaveButton(true);
				display.setEnabledCancelButton(true);
				String violationsDetails = "";
				for (ConstraintViolation<?> voilation : violations) {
					violationsDetails = voilation.getMessage() + "\n";
				}
				display.showError(violationsDetails);
			}

			@Override
			public void onSuccess(Void response) {
				display.setEnabledSaveButton(true);
				display.setEnabledCancelButton(true);
				// Global Status Window
				placeController().goTo(new MonitoringPlace());
			}
		});
	}

	@Override
	public void onAnalyticsTaskSelect(AnalyticsTaskProxy task) {
		display.getTaskMetricsListBox().setValue(null);
		display.setAcceptableOutputs(new ArrayList<AnalyticsOperationOutputProxy>());

		dao().createAnalyticsTaskRequest().listAllOutputs(task.getId()).fire(new Receiver<List<AnalyticsOperationOutputProxy>>() {
			@Override
			public void onSuccess(List<AnalyticsOperationOutputProxy> listOfOutputs) {

				if (editableOutput != null) {
					for (AnalyticsOperationOutputProxy metric : listOfOutputs) {
						if (metric.getId() == editableOutput.getId()) {
							logger.info("removed metric is" + metric.getName());
							listOfOutputs.remove(metric);
							break;
						}
					}
					editableOutput = null;
				}

				display.getTaskMetricsListBox().setValue(null);
				display.setAcceptableOutputs(listOfOutputs);
			}
		});
	}

	/**
	 * Helper function to sort list of email addresses
	 * 
	 * @param personList
	 */
	public void sortEmailAddresses(List<String> emailAddresses) {
		Collections.sort(emailAddresses, new Comparator<String>() {
			public int compare(String email1, String email2) {
				return email1.compareTo(email2);
			}
		});
	}

	/**
	 * Helper function to sort list of analytics tasks
	 * 
	 * @param taskList
	 */
	public void sortTaskList(List<AnalyticsTaskProxy> taskList) {
		Collections.sort(taskList, new Comparator<AnalyticsTaskProxy>() {
			public int compare(AnalyticsTaskProxy task1, AnalyticsTaskProxy task2) {
				return task1.getName().compareTo(task2.getName());
			}
		});
	}

	@Override
	public void onSelectOfPublicAcessCheckBox(boolean isPublicValue) {
		if (currentPerson().getRoles().contains(IPerson.Role.ADMIN)) {
			display.setVisibleAlertSubscriptionPanel(isPublicValue);
		}
	}

	@Override
	public void onCancel() {
		placeController().goTo(new MonitoringPlace());
	}
}