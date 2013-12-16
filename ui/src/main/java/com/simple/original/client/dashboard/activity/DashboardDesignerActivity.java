package com.simple.original.client.dashboard.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.simple.original.client.activity.AbstractActivity;
import com.simple.original.client.dashboard.IDashboardWidget;
import com.simple.original.client.dashboard.IWidgetEditor;
import com.simple.original.client.dashboard.designer.IDashboardDesignerView;
import com.simple.original.client.dashboard.designer.WidgetEditorFactory;
import com.simple.original.client.dashboard.events.WidgetAddedEvent;
import com.simple.original.client.dashboard.events.WidgetSelectedEvent;
import com.simple.original.client.place.AnalyticsTaskExecPlace;
import com.simple.original.client.place.DashboardDesignerPlace;
import com.simple.original.client.proxy.AnalyticsOperationOutputProxy;
import com.simple.original.client.proxy.AnalyticsTaskProxy;
import com.simple.original.client.proxy.DashboardProxy;
import com.simple.original.client.service.DaoRequestFactory.AnalyticsTaskRequest;
import com.simple.original.client.service.DaoRequestFactory.DashboardRequest;

public class DashboardDesignerActivity extends
		AbstractActivity<DashboardDesignerPlace, IDashboardDesignerView>
		implements IDashboardDesignerView.Presenter, WidgetSelectedEvent.Handler, WidgetAddedEvent.Handler  {

	/**
	 * The Logger
	 */
	private static final Logger logger = Logger
			.getLogger("DashboardDesignerActivity");

	/**
	 * This is our list of execution metrics for the widgets to select from.
	 */
	private final List<AnalyticsOperationOutputProxy> availableOuptuts = new ArrayList<AnalyticsOperationOutputProxy>();

	
	private final WidgetEditorFactory widgetEditorFactory;
	
	/**
	 * Default constructor is used to construct and defaults to
	 * clientFactory.getDashboardDesignerView for fetching it's view.
	 * 
	 * @param place
	 * @param clientFactory
	 */
	@Inject
	public DashboardDesignerActivity(IDashboardDesignerView view, WidgetEditorFactory widgetEditorFactory) {
		super(view);
		this.widgetEditorFactory = widgetEditorFactory;

	}

	@Override
	public void onTaskChanged(Long taskId) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCancelEdit() {
		placeController().goBackOr(new AnalyticsTaskExecPlace());
	}

	@Override
	public List<AnalyticsOperationOutputProxy> getOutputs() {
		return availableOuptuts;
	}

	@Override
	protected void bindToView() {
		display.setPresenter(this);
		eventBus().addHandler(WidgetSelectedEvent.TYPE, this);
		eventBus().addHandler(WidgetAddedEvent.TYPE, this);
	}

	/**
	 * This will fetch the last execution of the task specified as id along with
	 * all the execution metrics so that the dashboard can render it's widgets
	 * while editing.
	 * 
	 * @param id
	 *            The id of the {@link AnalyticsTaskProxy} to retrieve
	 * @return An analyticsrequest so that it can be chained.
	 */
	private AnalyticsTaskRequest fetchAllOperationOutputs(
			AnalyticsTaskRequest context, Long dashboardId) {
		context.listAllOutputs(dashboardId).to(
				new Receiver<List<AnalyticsOperationOutputProxy>>() {

					@Override
					public void onSuccess(
							List<AnalyticsOperationOutputProxy> response) {
						availableOuptuts.addAll(response);
					}
				});
		return context;
	}


	private void editDashboard() {
		DashboardProxy dashboard = place().getDashboard();

		if (dashboard != null) {
			logger.finest("Place is DashboardInitializePlace");
			doEdit(dao().createDashboardRequest(), dashboard);

		} else if (place().getDashboardId() != null) {
			fetchLastDashboardExecution(place().getDashboardId());
		}
	}

	/**
	 * Used to fetch a dashboard with it's bound last execution.
	 * 
	 * @param dashboardId
	 */
	private void fetchLastDashboardExecution(Long dashboardId) {
		dao().createDashboardRequest().getDashboardForDesigner(dashboardId)
				.fire(new Receiver<DashboardProxy>() {

					@Override
					public void onSuccess(DashboardProxy response) {
						if (response == null) {
							display.showError("Unable to retrieve dashboard so we cannot continue");
							return;
						}
						doEdit(dao().createDashboardRequest(), response);
					}
				});
	}

	/**
	 * This will first get the list of analytics tasks from the server before
	 * calling edit so that the get analytics task events won't call multiples.
	 * The other option is to buffer the requests and then send then a response
	 * in one call.
	 * 
	 * private void edit() { analyticsTaskRequest().listAll().fire(new
	 * Receiver<List<AnalyticsTaskProxy>>() {
	 * 
	 * @Override public void onSuccess(List<AnalyticsTaskProxy> response) {
	 *           availableTasks = response; doEdit(); } }); }
	 */

	private void doEdit(DashboardRequest context, DashboardProxy dashboard) {
		//display.setDashboardModel(dashboard);
		// widgetController = new WidgetControllerDesigner(eventBus,
		// clientFactory.getResources());
		// display.setWidgetController(widgetController);
	}

	@Override
	public void onWidgetSelected(WidgetSelectedEvent event) {
		IDashboardWidget<?> widget = event.getSelectedWidget();
		displayModelEditor(widget);
	}

	@Override
	public void onWidgetAdd(WidgetAddedEvent event) {
		IDashboardWidget<?> widget = event.getCreatedWidget();
		displayModelEditor(widget);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	void displayModelEditor(IDashboardWidget<?> widget) {
		String modelType = widget.getModel().getType();
		IWidgetEditor modelEditor = widgetEditorFactory.create(modelType);
		modelEditor.setDashboardWidget(widget);
		display.getWidgetPropertiesPanel().setPropertiesEditor(modelEditor);
		
		display.showWidgetPropertiesPanel(true);
	}
}