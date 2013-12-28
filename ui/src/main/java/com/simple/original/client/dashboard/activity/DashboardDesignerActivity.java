package com.simple.original.client.dashboard.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.simple.original.client.activity.AbstractActivity;
import com.simple.original.client.dashboard.IDashboardWidget;
import com.simple.original.client.dashboard.IWidgetEditor;
import com.simple.original.client.dashboard.designer.IDashboardDesignerView;
import com.simple.original.client.dashboard.designer.WidgetEditorFactory;
import com.simple.original.client.dashboard.events.WidgetAddedEvent;
import com.simple.original.client.dashboard.events.WidgetSelectedEvent;
import com.simple.original.client.dashboard.model.IDashboardWidgetsModel;
import com.simple.original.client.dashboard.model.jso.DashboardWidgetsModelJso;
import com.simple.original.client.place.DashboardDesignerPlace;
import com.simple.original.client.place.DashboardsPlace;
import com.simple.original.client.proxy.AnalyticsOperationOutputProxy;
import com.simple.original.client.proxy.AnalyticsTaskProxy;
import com.simple.original.client.proxy.DashboardProxy;
import com.simple.original.client.service.DaoRequestFactory.AnalyticsTaskRequest;
import com.simple.original.client.service.DaoRequestFactory.DashboardRequest;

public class DashboardDesignerActivity extends AbstractActivity<DashboardDesignerPlace, IDashboardDesignerView> implements IDashboardDesignerView.Presenter,
		WidgetSelectedEvent.Handler, WidgetAddedEvent.Handler {

	/**
	 * The Logger
	 */
	private static final Logger logger = Logger.getLogger("DashboardDesignerActivity");

	/**
	 * This is our list of execution metrics for the widgets to select from.
	 */
	private final List<AnalyticsOperationOutputProxy> availableOuptuts = new ArrayList<AnalyticsOperationOutputProxy>();

	private final WidgetEditorFactory widgetEditorFactory;

	private DashboardProxy dashboard;

	private DashboardRequest request;

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
	public List<AnalyticsOperationOutputProxy> getOutputs() {
		return availableOuptuts;
	}

	@Override
	protected void bindToView() {
		display.setPresenter(this);
		eventBus().addHandler(WidgetSelectedEvent.TYPE, this);
		eventBus().addHandler(WidgetAddedEvent.TYPE, this);

		Long dashboardId = place().getDashboardId();

		if (dashboardId != null && dashboardId.longValue() > 0) {
			fetchAndEdit(dashboardId);
		} else {
			createAndEdit();
		}
	}

	/**
	 * Will fetch and edit a dashboard from the server.
	 * 
	 * @param dashboardId
	 */
	private void fetchAndEdit(Long dashboardId) {
		logger.finest("Entering fetchAndEdit()");
		dao().createDashboardRequest().find(dashboardId).to(new Receiver<DashboardProxy>() {

			@Override
			public void onSuccess(DashboardProxy response) {
				DashboardDesignerActivity.this.dashboard = response;
				doEdit(response);
			}
		});
	}

	/**
	 * Creates a new dashboard and calls edit.
	 */
	private void createAndEdit() {
		logger.finest("Entering createAndEdit()");
		request = dao().createDashboardRequest();
		dashboard = request.create(DashboardProxy.class);
		String json = DashboardWidgetsModelJso.create().toJson();
		Window.alert("The json " + json);
		logger.info("WidgetModel is " + json);
		dashboard.setWidgetModel(DashboardWidgetsModelJso.create().toJson());
		doEdit(dashboard);
	}

	private void doEdit(DashboardProxy dashboard) {
		display.setName(dashboard.getName());
		display.setDescription(dashboard.getDescription());
		display.setDashboardModel(DashboardWidgetsModelJso.fromJson(dashboard.getWidgetModel()));
	}

	@Override
	public void onWidgetSelected(WidgetSelectedEvent event) {
		IDashboardWidget<?> widget = event.getSelectedWidget();
		displayModelEditor(widget);
	}

	/**
	 * Event handler for when a widget is added to the view.
	 */
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

	@Override
	public void onSave() {
		String dashboardName = display.getName();
		String dashboardDescription = display.getDescription();

		dashboard.setName(dashboardName);
		dashboard.setDescription(dashboardDescription);
		IDashboardWidgetsModel model = display.getDashboardModel();
		dashboard.setWidgetModel(model.toJson());

		Window.alert("saving dashboard with name " + dashboardName + " description " + dashboardDescription + model.toJson());

	}

	@Override
	public void onCancelEdit() {
		logger.finest("Calling cancel");
		placeController().goBackOr(new DashboardsPlace());
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
	private AnalyticsTaskRequest fetchAllOperationOutputs(AnalyticsTaskRequest context, Long dashboardId) {
		context.listAllOutputs(dashboardId).to(new Receiver<List<AnalyticsOperationOutputProxy>>() {

			@Override
			public void onSuccess(List<AnalyticsOperationOutputProxy> response) {
				availableOuptuts.addAll(response);
			}
		});
		return context;
	}

}