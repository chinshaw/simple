package com.simple.original.client.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.simple.original.client.events.NotificationEvent;
import com.simple.original.client.place.AnalyticsTaskExecPlace;
import com.simple.original.client.place.DashboardPlace;
import com.simple.original.client.proxy.AnalyticsOperationInputProxy;
import com.simple.original.client.proxy.AnalyticsOperationProxy;
import com.simple.original.client.proxy.AnalyticsTaskNameProxy;
import com.simple.original.client.proxy.AnalyticsTaskProxy;
import com.simple.original.client.proxy.CloneUtils;
import com.simple.original.client.service.DaoRequestFactory.AnalyticsTaskRequest;
import com.simple.original.client.service.ServiceRequestFactory.DashboardRequest;
import com.simple.original.client.view.IAnalyticsTaskExecutionView;
import com.simple.original.client.view.IAnalyticsTaskExecutionView.Filter;
import com.simple.original.client.view.IAnalyticsTaskExecutionView.Presenter;

public class AnalyticsTaskExecActivity extends AbstractActivity<AnalyticsTaskExecPlace, IAnalyticsTaskExecutionView> implements Presenter {

	private static final String LAST_SELECTED_TASK_COOKIE = "default_task_id";

	private DashboardRequest inputsRequest;

	// TODO THIS NEEDS TO GO SOMEWHERE ELSE.
	private class SaveAnalyticsTaskNamePopup extends PopupPanel implements HasClickHandlers {
		private FlowPanel panel = new FlowPanel();
		private TextBox nameBox = new TextBox();
		private Button saveButton = new Button("Save");
		private Button closeButton = new Button("Cancel");

		public SaveAnalyticsTaskNamePopup() {
			setHeight("200px");
			setWidth("400px");
			setGlassEnabled(true);
			setAnimationEnabled(true);

			this.setWidget(panel);
			panel.add(new Label("Please provide a valid name"));

			panel.add(nameBox);

			FlowPanel fp = new FlowPanel();
			fp.setHeight("30px");
			fp.add(saveButton);
			fp.add(closeButton);
			panel.add(fp);

			closeButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					hide();
				}
			});
		}

		public String getAnalyticsTaskName() {
			return nameBox.getValue();
		}

		public void validate() throws Exception {
			if (getAnalyticsTaskName().isEmpty()) {
				throw new Exception("Name may not be empty");
			}
			if (getAnalyticsTaskName().length() <= 3) {
				throw new Exception("Please give a descriptive name for cataloging, the minimum description is 10 characters");
			}
		}

		@Override
		public HandlerRegistration addClickHandler(ClickHandler handler) {
			return saveButton.addClickHandler(handler);
		}
	}

	/*
	 * Logger instance
	 */
	private static final Logger logger = Logger.getLogger(AnalyticsTaskExecActivity.class.getName());

	/**
	 * This is the currently selected analytics task when loaded.
	 */

	@Inject
	public AnalyticsTaskExecActivity(IAnalyticsTaskExecutionView view) {
		super(view);
		logger.fine("AnalyticsTaskExecPlace -> ANALYTICS_TASK ");
	}

	/**
	 * This function will bind the activity to the view.
	 */
	@Override
	protected void bindToView() {
		display.setPresenter(this);
		display.setRequestContext(inputsRequest);

		// Hook up the execute script handler so when the user clicks the button
		// we do something.
		HandlerRegistration clickHandler = display.getExecuteScriptButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				runAnalyticsTask();
			}
		});
		// Add handler so it will be cleaned up.
		handlers.add(clickHandler);
		updateTasks();
	}

	/**
	 * This method will update the list of tasks and assign them to the view.
	 */
	public void updateTasks() {
		Receiver<List<AnalyticsTaskNameProxy>> receiver = new Receiver<List<AnalyticsTaskNameProxy>>() {

			@Override
			public void onSuccess(List<AnalyticsTaskNameProxy> tasks) {
				display.setAcceptableAnalyticsTasks(tasks);

				logger.fine("LAST_SELECTED_TASK_COOKIE ->" + LAST_SELECTED_TASK_COOKIE);
				// See if the cookie has the last run task and we
				// will set it to use that.
				String lastTaskId = Cookies.getCookie(LAST_SELECTED_TASK_COOKIE);
				if (lastTaskId != null) {
					// Cookie had a last run analytics task so we will use
					// that.
					for (AnalyticsTaskNameProxy task : tasks) {
						if (task.getId() == Long.parseLong(lastTaskId)) {
							display.setSelectedAnalyticsTask(task);
						}
					}
				}
			}
		};

		RequestContext context = null;

		logger.info("Getting tasks");
		if (display.getFilter() == Filter.All) {
			context = dao().createAnalyticsTaskRequest().listAllTaskNames().to(receiver);
		} else {
			context = dao().createAnalyticsTaskRequest().listTaskNamesForCurrentPerson().to(receiver);
		}

		context.fire();
	}

	/**
	 * This method is called when user clicks on Execute button and script gets
	 * executed.
	 */
	private void runAnalyticsTask() {
		List<AnalyticsOperationInputProxy> inputs = (List<AnalyticsOperationInputProxy>) display.getInputsEditorDriver().flush();
		DashboardPlace dashboardPlace = new DashboardPlace(display.getSelectedTaskId(), inputs);

		placeController().goTo(dashboardPlace);
	}

	/**
	 * This method is used to update script inputs
	 * 
	 * @param operations
	 */
	private void updateInputs(List<AnalyticsOperationProxy> operations) {
		List<AnalyticsOperationInputProxy> inputs = new ArrayList<AnalyticsOperationInputProxy>();
		for (AnalyticsOperationProxy operation : operations) {
			for (AnalyticsOperationInputProxy input : operation.getInputs()) {

				AnalyticsOperationInputProxy clone = CloneUtils.clone(inputsRequest, input);
				input = inputsRequest.edit(clone);
				inputs.add(input);
			}
		}
		// List<AnalyticsOperationInputProxy> arguments = new
		// ArrayList<AnalyticsOperationInputProxy>();
		// DashboardActivity.processArguments(inputsRequest,arguments , inputs);

		display.getInputsEditorDriver().edit(inputs);
	}

	/**
	 * This method is used to retain the previous selected Analytics task from
	 * the cookie and display it within the same session.
	 */
	@Override
	public void onAnalyticsTaskSelected(AnalyticsTaskNameProxy selectedTask) {
		if (selectedTask != null) {
			Cookies.setCookie(LAST_SELECTED_TASK_COOKIE, selectedTask.getId().toString());
			fetchTaskProperties(selectedTask.getId());

		} else {
			// need to clear the panels
			display.setTaskDescription("No analytics task selected");
			display.getInputsEditorDriver().edit(new ArrayList<AnalyticsOperationInputProxy>());
		}
	}

	/**
	 * When the save button is clicked on a analytics task we will save a new
	 * analytics task with the user as the owner. We will also take the selected
	 * inputs and save them as the default inputs of the analytics task.
	 */
	@Override
	public void onSaveTask() {
		final SaveAnalyticsTaskNamePopup popup = new SaveAnalyticsTaskNamePopup();

		popup.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				try {
					// This may throw an exception and if so it request will not
					// be saved.
					popup.validate();

					dao().createAnalyticsTaskRequest().copy(display.getSelectedTaskId()).with(AnalyticsTaskProxy.EDIT_PROPERTIES).fire(new Receiver<AnalyticsTaskProxy>() {

						@Override
						public void onSuccess(AnalyticsTaskProxy analyticsTask) {
							AnalyticsTaskRequest request = dao().createAnalyticsTaskRequest();
							analyticsTask = request.edit(analyticsTask);

							analyticsTask.setName(popup.getAnalyticsTaskName());

							request.saveAndReturn(analyticsTask).with("script").with("script.inputs").fire(new Receiver<AnalyticsTaskProxy>() {

								@Override
								public void onSuccess(AnalyticsTaskProxy analyticsTask) {
									popup.hide();
									eventBus().fireEvent(new NotificationEvent("Task was saved successfully"));
								}
							});
						}
					});

				} catch (Exception e) {
					Window.alert("Error: " + e.getMessage());
				}
			}
		});

		popup.center();
	}

	private void fetchTaskProperties(Long taskId) {
		AnalyticsTaskRequest request = dao().createAnalyticsTaskRequest();

		request.find(taskId).to(new Receiver<AnalyticsTaskProxy>() {

			@Override
			public void onSuccess(AnalyticsTaskProxy analyticsTask) {
				display.setTaskDescription(analyticsTask.getDescription());

			}
		});

		request.listAllInputs(taskId).to(new Receiver<List<AnalyticsOperationInputProxy>>() {

			@Override
			public void onSuccess(List<AnalyticsOperationInputProxy> response) {
				List<AnalyticsOperationInputProxy> inputs = new ArrayList<AnalyticsOperationInputProxy>();
				for (AnalyticsOperationInputProxy input : response) {
					AnalyticsOperationInputProxy clone = CloneUtils.clone(inputsRequest, input);
					input = inputsRequest.edit(clone);
					inputs.add(input);
				}
				display.getInputsEditorDriver().edit(inputs);
			}
		});
		request.fire();
	}

	/**
	 * Schedule the selected Task with the selected inputs.
	 */
	@Override
	public void onScheduleTask() {
		throw new RuntimeException("Not implemented");
	}
}