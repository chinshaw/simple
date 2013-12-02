package com.simple.original.client.dashboard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.dashboard.designer.DroppablePanel;
import com.simple.original.client.dashboard.events.WidgetAddEvent;
import com.simple.original.client.dashboard.events.WidgetModelChangedEvent;
import com.simple.original.client.dashboard.events.WidgetRemoveEvent;
import com.simple.original.client.dashboard.events.WidgetSelectedEvent;
import com.simple.original.client.dashboard.model.IPanelWidgetModel;
import com.simple.original.client.dashboard.model.IWidgetModel;
import com.simple.original.client.resources.Resources;

public class PanelWidget extends AbstractDashboardWidget<IPanelWidgetModel>
		implements IInspectable,	
		WidgetAddEvent.Handler, WidgetModelChangedEvent.Handler,
		WidgetRemoveEvent.Handler {

	public interface Binder extends UiBinder<Widget, PanelWidget> {
	}

	/**
	 * This is the uibinder and it will use the view.DefaultView.ui.xml
	 * template.
	 */
	@UiField(provided = true)
	DroppablePanel widgetsPanel;

	@UiField
	LabelElement panelTitle;

	private final WidgetFactory widgetFactory;

	@Inject
	public PanelWidget(final EventBus eventBus, final Resources resources, WidgetFactory widgetFactory) {
		super(eventBus, resources);
		this.widgetFactory = widgetFactory;
		widgetsPanel = new DroppablePanel(widgetFactory);
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		
		widgetsPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		addDomHandler(widgetSelectedHandler, ClickEvent.getType());
	}
	
	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public void onWidgetModelChanged(WidgetModelChangedEvent event) {
		IWidgetModel eventModel = event.getWidgetModel();
		if (eventModel == model) {
			panelTitle.setInnerText(model.getTitle());
		}
	}

	@Override
	public void onWidgetAdd(WidgetAddEvent event) {
		if (event.getContainsWidgets() == getModel()) {
			widgetsPanel.add(event.getCreatedWidget().asWidget());
		}
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
	public void setModel(IPanelWidgetModel model) {
		this.model = model;
		panelTitle.setInnerText(model.getTitle());
		
		for (IWidgetModel widgetModel : model.getWidgets()) {
			IDashboardWidget<?> widget = widgetFactory.createWidget(widgetModel);
			widgetsPanel.add(widget);
		}
	}

	@Override
	protected void onWidgetSelected(NativeEvent event) {
		eventBus.fireEvent(new WidgetSelectedEvent(this));
	}
	
	public int getWidgetCount() {
		return widgetsPanel.getWidgetCount();
	}

	@Override
	public ImageResource getSelectorIcon() {
		return resources.panelWidgetSelector();
	}
}