package com.simple.original.client.dashboard;

import org.apache.commons.httpclient.HeaderElement;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.dashboard.designer.DroppablePanel;
import com.simple.original.client.dashboard.events.WidgetModelChangedEvent;
import com.simple.original.client.dashboard.events.WidgetRemoveEvent;
import com.simple.original.client.dashboard.model.IPanelWidgetModel;
import com.simple.original.client.dashboard.model.IWidgetModel;
import com.simple.original.client.resources.Resources;

public class PanelWidget extends AbstractDashboardWidget<IPanelWidgetModel>
		implements IInspectable, WidgetModelChangedEvent.Handler,
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
	HeaderElement panelTitle;

	private final WidgetFactory widgetFactory;

	@Inject
	public PanelWidget(final EventBus eventBus, final Resources resources, WidgetFactory widgetFactory, IPanelWidgetModel model) {
		super(eventBus, resources);
		this.model = model;
		this.widgetFactory = widgetFactory;
		widgetsPanel = new DroppablePanel(widgetFactory, eventBus);
		widgetsPanel.setWidgetModel(model);
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		widgetsPanel.getElement().getStyle().setPosition(Position.RELATIVE);
	}
	
	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public void onWidgetModelChanged(WidgetModelChangedEvent event) {
		IWidgetModel eventModel = event.getWidgetModel();
		if (eventModel == model) {
			panelTitle.setValue(model.getTitle());
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

	
	/**
	 * This is injected and the dashboard module will inject a new model when it is created.
	 * This can also be used to set the model at a later time.
	 
	@Override
	public void setModel(IPanelWidgetModel model) {
		this.model = model;
		update(); 
		
		for (IWidgetModel widgetModel : model.getWidgets()) {
			IDashboardWidget<?> widget = widgetFactory.createWidget(widgetModel);
			widgetsPanel.add(widget);
		}
	}
	*/
	
	public int getWidgetCount() {
		return widgetsPanel.getWidgetCount();
	}

	@Override
	public ImageResource getSelectorIcon() {
		return resources.panelWidgetSelector();
	}
	
	public void update() {
		panelTitle.setValue(model.getTitle());
	}
}