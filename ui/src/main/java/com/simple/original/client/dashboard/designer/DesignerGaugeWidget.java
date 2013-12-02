 package com.simple.original.client.dashboard.designer;

import com.google.gwt.resources.client.ImageResource;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.dashboard.GaugeWidget;
import com.simple.original.client.dashboard.model.IGaugeWidgetModel;
import com.simple.original.client.resources.Resources;

public class DesignerGaugeWidget extends DesignerWidget<GaugeWidget, IGaugeWidgetModel, GaugeEditor> {
	
	public DesignerGaugeWidget(final EventBus eventBus, final Resources resources, final IGaugeWidgetModel model, GaugeEditor editor) {
		super(eventBus, new GaugeWidget(eventBus, resources), editor);
		
		setStyleName(resources.style().designerGaugeWidget());
		
		// Set this so the gauge will always update correctly.
		getWrappedWidget().setFastDraw(false);
		
		initContextHandlers();
		initDraggable();
	}

	@Override
	public void setModel(IGaugeWidgetModel model) {
		getWrappedWidget().setModel(model);
	}

	@Override
	public void setTitle(String title) {
		getWrappedWidget().setTitle(title);
	}

	@Override
	public ImageResource getSelectorIcon() {
		return null;
	}
}