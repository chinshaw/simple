package com.simple.original.client.dashboard.designer;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.simple.original.client.dashboard.WidgetFactory;
import com.simple.original.client.dashboard.events.WidgetRemoveEvent;
import com.simple.original.client.dashboard.model.IDashboardWidgetsModel;
import com.simple.original.client.dashboard.model.IWidgetModel;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.desktop.AbstractView;
import com.simple.original.client.view.widgets.ErrorPanel;

/**
 * @author chinshaw
 */
public class DashboardDesignerView extends AbstractView implements IDashboardDesignerView {

	private static final Logger logger = Logger.getLogger(DashboardDesignerView.class.getName());

	/**
	 * This is the uibinder and it will use the view.DefaultView.ui.xml
	 * template.
	 */
	public interface Binder extends UiBinder<Widget, DashboardDesignerView> {
	}

	/**
	 * This is the actual widget model that we are editng, this may go away
	 * sooner than later.
	 */
	private IDashboardWidgetsModel model = null;

	@UiField(provided = true)
	SplitLayoutPanel container;

	@UiField
	TextBox name;

	@UiField
	TextBox description;

	@UiField(provided = true)
	DroppablePanel widgetsPanel;

	@UiField
	ErrorPanel errorPanel;

	//@UiField(provided = true)
	//WidgetPalettePanel widgetPalettePanel;

	@UiField(provided = true)
	WidgetPropertiesPanel widgetPropertiesPanel;

	private Presenter presenter;

	private static EventBus designerEventBus = new SimpleEventBus();

	@Inject
	public DashboardDesignerView(EventBus eventBus, Resources resources, WidgetPalettePanel widgetPalettePanel, WidgetPropertiesPanel widgetPropertiesPanel,
			WidgetFactory widgetFactory) {

		super(designerEventBus, resources);
		//this.widgetPalettePanel = widgetPalettePanel;
		this.widgetPropertiesPanel = widgetPropertiesPanel;
		this.widgetsPanel = new DroppablePanel(widgetFactory, eventBus);
		container = new SplitLayoutPanel(5);

		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

		//container.getWidgetContainerElement(widgetPalettePanel).getStyle().setOverflow(Overflow.VISIBLE);
		hookupEvents();
	}

	private void hookupEvents() {
		getEventBus().addHandler(WidgetRemoveEvent.TYPE, this);
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

	@Override
	public IDashboardWidgetsModel getDashboardModel() {
		return this.model;
	}
	
	@Override
	public void setDashboardModel(IDashboardWidgetsModel dashboard) {
		if (dashboard == null) {
			throw new IllegalArgumentException("Dashboard cannot be null");
		}

		this.model = dashboard;

		if (this.model.getWidgets() == null) {
			this.model.setWidgets(new ArrayList<IWidgetModel>());
		}
		widgetsPanel.setWidgetModel(model);
	}
 
	@Override
	public void onWidgetRemove(WidgetRemoveEvent event) {
		if (model.getWidgets().contains(event.getSelectedWidget())) {
			model.getWidgets().remove(event.getSelectedWidget());
		}
	}

	@Override
	public EventBus getEventBus() {
		return designerEventBus;
	}

	public DockLayoutPanel getLayoutPanel() {
		return container;
	}

	@Override
	public DroppablePanel getDroppbalePanel() {
		return widgetsPanel;
	}

	@Override
	public WidgetPalettePanel getWidgetPalettePanel() {
	//	return widgetPalettePanel;
		return null;
	}

	@Override
	public WidgetPropertiesPanel getWidgetPropertiesPanel() {
		return widgetPropertiesPanel;
	}

	@Override
	public void showWidgetPropertiesPanel(boolean show) {
		container.animate(800);
		if (show) {
			container.setWidgetSize(getWidgetPropertiesPanel(), 250);
			container.animate(800);
		} else {
			container.setWidgetSize(getWidgetPropertiesPanel(), 0);
			container.animate(800);
		}
	}

	@UiHandler("saveDashboard")
	void onSaveDashboard(ClickEvent click) {
		presenter.onSave();
	}

	@Override
	public String getName() {
		return name.getValue();
	}

	@Override
	public void setName(String name) {
		this.name.setValue(name);
	}
	
	@Override
	public String getDescription() {
		return description.getValue();
	}
	
	@Override
	public void setDescription(String description) {
		this.description.setValue(description);
	}
}