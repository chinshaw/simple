package com.simple.original.client.dashboard.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Image;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.EntityProxyChange;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.client.activity.AbstractActivity;
import com.simple.original.client.dashboard.events.DashboardNavigationEvent;
import com.simple.original.client.events.DashboardChangedEvent;
import com.simple.original.client.events.HandlerCollection;
import com.simple.original.client.place.ApplicationPlace;
import com.simple.original.client.place.DashboardDesignerPlace;
import com.simple.original.client.place.DashboardPlace;
import com.simple.original.client.place.HistoricalMetricsPlace;
import com.simple.original.client.place.LatestDashboardPlace;
import com.simple.original.client.proxy.AnalyticsOperationInputProxy;
import com.simple.original.client.proxy.AnalyticsTaskExecutionProxy;
import com.simple.original.client.proxy.DashboardProxy;
import com.simple.original.client.service.ServiceRequestFactory.AnalyticsRequest;
import com.simple.original.client.utils.ClientUtils;
import com.simple.original.client.view.IDashboardView;
import com.simple.original.client.view.IDashboardView.Presenter;
import com.simple.original.client.view.desktop.AnalyticsInputEditor;
import com.simple.original.client.view.widgets.ProgressWidget;
import com.simple.original.shared.AnalyticsTaskExecutionEvent;
import com.simple.original.shared.AnalyticsTaskOutputEvent;

public class DashboardActivity extends
		AbstractActivity<ApplicationPlace, IDashboardView> implements
		Presenter, DashboardNavigationEvent.Handler,
		EntityProxyChange.Handler<AnalyticsTaskExecutionProxy>,
		AnalyticsTaskExecutionEvent.Handler, AnalyticsTaskOutputEvent.Handler {

	/**
	 * Analytics task receiver that is used to update screen when task response
	 * is received.
	 */
	protected Receiver<DashboardProxy> dashboardReceiver = new Receiver<DashboardProxy>() {

		@Override
		public void onSuccess(DashboardProxy response) {
			showUpdatingPanel(false);
			if (response == null) {
				display.showError("There is no previous execution of this task, execute the task atleast once to display this dashboard");
				return;
			}
			dashboard = response;
			processResponse(response);
		}

		@Override
		public void onFailure(ServerFailure failure) {
			showUpdatingPanel(false);
			display.showError("Unable to execute analytics task", failure);
		}
	};

	/**
	 * Called to edit the dashboard using the execution.
	 */
	private Command editDashboardCommand = new Command() {

		@Override
		public void execute() {
			placeController().goTo(
					new DashboardDesignerPlace(dashboard.getId()));
		}
	};

	/**
	 * Called to edit the dashboard using the execution.
	 */
	private Command editTaskCommand = new Command() {

		@Override
		public void execute() {
			Window.alert("TODO FIX ME DashboardActivity + editCommand");
		}
	};

	// final TextArea output = new TextArea();

	/**
	 * Progress updating widget.
	 */
	private ProgressWidget overlayWidget;

	/**
	 * {@see java.util.Logger}
	 */
	private static final Logger logger = Logger
			.getLogger(DashboardActivity.class.getName());

	private AnalyticsRequest request;

	private DashboardProxy dashboard;

	private Map<Date, Long> dateSliderMap = new HashMap<Date, Long>();

	private HandlerCollection handlers = new HandlerCollection();

	@Inject
	public DashboardActivity(IDashboardView view) {
		super(view);
	}

	/**
	 * This function will bind the activity to the view.
	 */
	@Override
	protected void bindToView() {
		display.setPresenter(this);

		addContextMenu();

		// Lets go ahead and run the script.
		runDashboardTask(getDashboardUpdateRequest());
		EntityProxyChange.registerForProxyType(eventBus(),
				AnalyticsTaskExecutionProxy.class, this);

		eventBus().addHandler(AnalyticsTaskExecutionEvent.TYPE, this);
		eventBus().addHandler(AnalyticsTaskOutputEvent.TYPE, this);
	}

	private void processResponse(DashboardProxy taskExecution) {
		showUpdatingPanel(false);

		if (taskExecution == null) {
			display.showError("There is no previous execution of this task, execute the task atleast once to display this dashboard");
			return;
		}

		// If we don't have a dashboard we should present the user with the
		// option to create a dashboard.
		if (dashboard == null) {
			if (Window
					.confirm("There is not dashboard, do you want to create one?")) {
				placeController().goTo(
						new DashboardDesignerPlace(dashboard.getId()));
			} else {
				display.showError("No dashboard created so there is nothing to show");
			}
		} else {
			renderDashboard(dashboard);
			updateInputsPanel(dashboard.getInputs());
			updateTimeMachine();
		}

		updateDebugLog();
	}

	private void renderDashboard(DashboardProxy dashboard) {
		// This will update the name of the panel to the description of the
		// dashboard.

		eventBus().fireEvent(new DashboardChangedEvent(dashboard));
	}

	protected AnalyticsRequest getDashboardUpdateRequest() {
		DashboardPlace dashboardPlace = (DashboardPlace) place();

		List<AnalyticsOperationInputProxy> arguments = dashboardPlace
				.getArguments();
		request = service().analyticsRequest();

		List<AnalyticsOperationInputProxy> inputs = new ArrayList<AnalyticsOperationInputProxy>();

		// If we have arguments from the ui let's use them.
		if (arguments != null) {
			ClientUtils.cloneOperationInputs(request, inputs, arguments);
			/*
			 * for (AnalyticsOperationInputProxy argument : arguments) {
			 * AnalyticsOperationInputProxy input =
			 * request.create(AnalyticsOperationInputProxy.class);
			 * 
			 * //ProxyUtils.cloneBeanProperties(argument, input, request);
			 * inputs.add(input); }
			 */

		} else {
			logger.info("Found no arguments for task");
		}

		Long taskId = ((DashboardPlace)place()).getAnalyticsTaskId();
		request.executeInteractive(
				taskId, inputs, null)
				.with(AnalyticsTaskExecutionProxy.EXECUTION_PROPERTIES)
				.to(dashboardReceiver);
		return request;
	}

	private void runDashboardTask(AnalyticsRequest request) {
		showUpdatingPanel(true);
		request.fire();
	}

	/**
	 * Show the updating panel.
	 */
	private void showUpdatingPanel(boolean show) {
		overlayWidget = new ProgressWidget(new Image(
					resources().updating()), "Running", resources().style());
		if (show) {
			display.getContentPanel().add(overlayWidget);
		} else {
			display.getContentPanel().remove(overlayWidget);
		}
	}

	private void addContextMenu() {
	}

	@Override
	public void onStop() {
		handlers.clear();
		super.onStop();
	}

	private void updateTimeMachine() {
		/*
		 * TODO FIX ME dateSliderMap.clear();
		 * daoRequestFactory().analyticsTaskExecutionRequest
		 * ().findRange(currentTaskId, -100, 100).fire(new
		 * Receiver<List<AnalyticsTaskExecutionProxy>>() {
		 * 
		 * @Override public void onSuccess(List<AnalyticsTaskExecutionProxy>
		 * response) { List<Date> datesList = new ArrayList<Date>(); for
		 * (AnalyticsTaskExecutionProxy taskExecution : response) {
		 * datesList.add(taskExecution.getCompletionTime());
		 * dateSliderMap.put(taskExecution.getCompletionTime(),
		 * taskExecution.getId()); }
		 * 
		 * display.setHistory(datesList); } });
		 */
	}

	public void start(AcceptsOneWidget parentPanel, EventBus eventBus) {
		super.start(parentPanel, eventBus);
		handlers.add(eventBus.addHandler(DashboardNavigationEvent.TYPE, this));
	}

	private void updateInputsPanel(List<AnalyticsOperationInputProxy> inputs) {
		if (inputs == null || inputs.size() == 0) {
			logger.info("There were no inputs");
			return;
		}
		request = service().analyticsRequest();
		AnalyticsInputEditor inputsEditor = display.getInputsEditor();
		List<AnalyticsOperationInputProxy> editable = edit(request, inputs);

		inputsEditor.setRequestContext(request);
		inputsEditor.getInputsEditorDriver().edit(editable);
	}

	private List<AnalyticsOperationInputProxy> edit(RequestContext context,
			List<AnalyticsOperationInputProxy> inputs) {
		List<AnalyticsOperationInputProxy> editable = new ArrayList<AnalyticsOperationInputProxy>();
		for (AnalyticsOperationInputProxy input : inputs) {
			input = context.edit(input);
			editable.add(input);
		}
		return editable;
	}

	@Override
	public void onRerunTask() {
		List<AnalyticsOperationInputProxy> inputs = (List<AnalyticsOperationInputProxy>) display
				.getInputsEditor().getInputsEditorDriver().flush();
		placeController().goTo(
				new DashboardPlace(dashboard.getId(), inputs));
	}

	@Override
	public void onDashboardNavigationEvent(DashboardNavigationEvent event) {
		if (event.getDashboardId() != null) {
			placeController().goTo(
					new LatestDashboardPlace(event.getDashboardId()));
		}
	}

	@Override
	public void onDateSliderChange(Date date) {
		Long taskExecutionId = dateSliderMap.get(date);
		if (taskExecutionId == null) {
			display.showError("Task execution id was null for the specified historical execution");
		} else {
			placeController().goTo(
					new HistoricalMetricsPlace(taskExecutionId));
		}
	}

	@Override
	public void onEditDashboard() {
		editDashboardCommand.execute();
	}

	@Override
	public void onProxyChange(
			EntityProxyChange<AnalyticsTaskExecutionProxy> event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onAnalyticsTaskExecution(AnalyticsTaskExecutionEvent event) {
		Long taskId = ((DashboardPlace)place()).getAnalyticsTaskId();
		logger.info("Task id is " + event.getTaskId() + " event task id is "
				+ taskId);
		if (event.getTaskId().longValue() == taskId
				.longValue()) {
			// Do something with the task execution
		}
	}


	public void updateDebugLog() {
	}

	@Override
	public void handleOut(String output) {
		// this.output.setText(this.output.getText() + output);
		// display.showTaskOutput(output);
	}

	@Override
	public void handleError(String error) {
		display.showTaskOutput(error);
	}
}