package com.simple.original.client.dashboard.designer;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.simple.original.client.dashboard.IWidgetModelEditor;
import com.simple.original.client.dashboard.WidgetFactory;
import com.simple.original.client.dashboard.events.WidgetAddEvent;
import com.simple.original.client.dashboard.events.WidgetRemoveEvent;
import com.simple.original.client.dashboard.model.IDashboardModel;
import com.simple.original.client.dashboard.model.IWidgetModel;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.desktop.AbstractView;
import com.simple.original.client.view.widgets.ErrorPanel;

/**
 * @author chinshaw
 */
public class DashboardDesignerView extends AbstractView implements
		IDashboardDesignerView, IWidgetModelEditor<IDashboardModel> {

	private static final Logger logger = Logger
			.getLogger(DashboardDesignerView.class.getName());

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
	private IDashboardModel model = null;

	@UiField
	DockLayoutPanel container;

	@UiField
	TextBox name;

	@UiField
	TextBox description;

	@UiField(provided = true)
	DroppablePanel widgetsPanel;

	@UiField
	ErrorPanel errorPanel;

	@UiField(provided = true)
	WidgetPalettePanel widgetPalettePanel;

	@UiField(provided = true)
	WidgetPropertiesPanel widgetPropertiesPanel;

	private Presenter presenter;

	private static EventBus designerEventBus = new SimpleEventBus();

	@Inject
	public DashboardDesignerView(EventBus eventBus, Resources resources,
			WidgetPalettePanel widgetPalettePanel,
			WidgetPropertiesPanel widgetPropertiesPanel,
			WidgetFactory widgetFactory) {
		
		super(designerEventBus, resources);
		this.widgetPalettePanel = widgetPalettePanel;
		this.widgetPropertiesPanel = widgetPropertiesPanel;
		this.widgetsPanel = new DroppablePanel(widgetFactory);
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

		container.getWidgetContainerElement(widgetPalettePanel).getStyle()
				.setOverflow(Overflow.VISIBLE);
		hookupEvents();
	}

	private void hookupEvents() {
		getEventBus().addHandler(WidgetAddEvent.TYPE, this);
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
	public void setDashboardModel(IDashboardModel dashboard) {
		if (dashboard == null) {
			throw new IllegalArgumentException("Dashboard cannot be null");
		}

		this.model = dashboard;

		if (this.model.getWidgets() == null) {
			this.model.setWidgets(new ArrayList<IWidgetModel>());
		}
	}

	@UiHandler("save")
	void onSave(ClickEvent event) {
		Window.alert("TODO");
	}

	@UiHandler("cancel")
	void onCancel(ClickEvent event) {
		presenter.onCancelEdit();
	}

	@Override
	public void onWidgetRemove(WidgetRemoveEvent event) {
		if (model.getWidgets().contains(event.getSelectedWidget())) {
			model.getWidgets().remove(event.getSelectedWidget());
		}
	}

	@Override
	public IDashboardModel getModel() {
		return model;
	}

	public void setModel(IDashboardModel dashboard) {
		this.model = dashboard;
	}

	@Override
	public EventBus getEventBus() {
		return designerEventBus;
	}

	@Override
	public void onWidgetAdd(WidgetAddEvent event) {
		if (event.getContainsWidgets() != null
				&& event.getContainsWidgets() == model) {
			this.model.getWidgets().add(event.getCreatedWidget().getModel());
		}
	}

	@Override
	public DroppablePanel getDroppbalePanel() {
		return widgetsPanel;
	}

	@Override
	public WidgetPalettePanel getWidgetPalettePanel() {
		return widgetPalettePanel;
	}

	@Override
	public WidgetPropertiesPanel getWidgetPropertiesPanel() {
		return widgetPropertiesPanel;
	}
}