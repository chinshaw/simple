package com.simple.original.client.dashboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.dashboard.events.WidgetAddEvent;
import com.simple.original.client.dashboard.events.WidgetRemoveEvent;
import com.simple.original.client.dashboard.model.IDashboardModel;
import com.simple.original.client.dashboard.model.IWidgetModel;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IDashboardView;
import com.simple.original.client.view.desktop.AbstractView;
import com.simple.original.client.view.desktop.AnalyticsInputEditor;
import com.simple.original.client.view.widgets.DateSlider;
import com.simple.original.client.view.widgets.ErrorPanel;
import com.simple.original.client.view.widgets.NewPopinPanel;
import com.simple.original.client.view.widgets.NewPopinPanel.Tab;

/**
 * @author chinshaw
 */
public class DashboardView extends AbstractView implements IDashboardView, IInspectable, WidgetAddEvent.Handler,
		WidgetRemoveEvent.Handler {

	/**
	 * This is the uibinder and it will use the view.DefaultView.ui.xml
	 * template.
	 */
	@UiTemplate("Dashboard.ui.xml")
	public interface Binder extends UiBinder<Widget, DashboardView> {
	}

	private static final Logger logger = Logger.getLogger(DashboardView.class
			.getName());

	@UiField
	FlowPanel widgetsPanel;

	@UiField
	Label taskExecutionDate;

	@UiField
	TextArea debugOutput;

	@UiField
	NewPopinPanel popinPanel;

	@UiField
	Tab debugTab;

	@UiField(provided = true)
	DateSlider dateSlider = new DateSlider() {

		@Override
		public void onDateValueChange(Date date) {
			onDateSliderChange(date);
		}
	};

	@UiField
	ErrorPanel errorPanel;

	@UiField
	AnalyticsInputEditor taskInputsEditor;

	private IDashboardModel model;

	private Presenter presenter;

	private final WidgetFactory widgetFactory;

	@Inject
	public DashboardView(EventBus eventBus, Resources resources, WidgetFactory widgetFactory) {
		super(eventBus, resources);
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		this.widgetFactory = widgetFactory;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/**
	 * @see com.simple.original.client.view.desktop.AbstractView#getErrorPanel()
	 */
	@Override
	protected ErrorPanel getErrorPanel() {
		return errorPanel;
	}

	@Override
	public void reset() {
		errorPanel.clear();
	}

	@Editor.Ignore
	@Override
	public AnalyticsInputEditor getInputsEditor() {
		return taskInputsEditor;
	}

	@Override
	public void setDebugOutput(String log) {
		debugOutput.setText(log);
	}

	@Override
	public HasWidgets getContentPanel() {
		return widgetsPanel;
	}

	@Override
	public void setModel(IDashboardModel model) {
		this.model = model;
		
		for (IWidgetModel widgetModel : model.getWidgets()) {
			IDashboardWidget<?> widget = widgetFactory.createWidget(widgetModel);
			widgetsPanel.add(widget);
		}
	}

	@Override
	public IWidgetModel getModel() {
		return model;
	}

	@UiHandler("rerunTask")
	void onReRunTask(ClickEvent event) {
		popinPanel.close();
		presenter.onRerunTask();
	}

	@Override
	public void setTaskCompletionTime(Date completionTime) {
		taskExecutionDate.setText("Viewing cached results of task executed at "
				+ DateTimeFormat.getFormat("MM/dd/yyyy  'at' hh:mm:ss a")
						.format(completionTime));
	}

	private void onDateSliderChange(Date date) {
		presenter.onDateSliderChange(date);
	}

	@Override
	public void showTaskOutput(String debugText) {
		debugOutput.setText(debugText);
	}

	@Override
	public void setHistory(List<Date> historicalDates) {
	}

	@Override
	public void onWidgetRemove(WidgetRemoveEvent event) {
		IDashboardWidget<?> selectedWidget = event.getSelectedWidget();
		widgetsPanel.remove(event.getSelectedWidget().asWidget());

		if (model.getWidgets().contains(selectedWidget.getModel())) {
			model.getWidgets().remove(selectedWidget.getModel());
		}
	}

	@Override
	public void onWidgetAdd(WidgetAddEvent event) {
		if (event.getContainsWidgets() == model
				|| event.getContainsWidgets() == null) {
			widgetsPanel.add(event.getCreatedWidget().asWidget());
		}
	}

	@Override
	public List<AbstractDashboardWidget<?>> getDashboardWidgets() {
		List<AbstractDashboardWidget<?>> widgets = new ArrayList<AbstractDashboardWidget<?>>();
		Iterator<Widget> iter = widgetsPanel.iterator();

		while (iter.hasNext()) {
			widgets.add((AbstractDashboardWidget<?>) iter.next());
		}
		return widgets;
	}
}