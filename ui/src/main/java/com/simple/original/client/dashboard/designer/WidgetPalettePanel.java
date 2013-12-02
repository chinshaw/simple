package com.simple.original.client.dashboard.designer;

import gwtquery.plugins.draggable.client.DraggableOptions.HelperType;
import gwtquery.plugins.draggable.client.gwt.DraggableWidget;

import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.dashboard.IDashboardWidget;
import com.simple.original.client.resources.Resources;

public class WidgetPalettePanel extends Composite {

	public class PaletteWidget extends DraggableWidget<Image> {

		private String modelType;

		public PaletteWidget(ImageResource imageResource, String modelType) {
			super(new Image(imageResource));
			this.modelType = modelType;
			
			setStyleName(resources.style().selectorWidget());
			getDraggableOptions().setHelper(HelperType.CLONE);
		}
		
		public String getModelType() {
			return modelType;
		}
	}

	private final FlowPanel container = new FlowPanel();
	private FlowPanel widgetPanel = new FlowPanel();
	private final Map<String, Provider<IDashboardWidget>> widgetProvider;
	private final Resources resources;
	
	@Inject
	public WidgetPalettePanel(EventBus eventBus, Resources resources, Map<String, Provider<IDashboardWidget>> widgetProvider) {
		this.widgetProvider = widgetProvider;
		this.resources = resources;
		initWidget(container);

		// Set styles
		widgetPanel.setStyleName(resources.style().widgetSelectorPanel());
		getElement().getStyle().setZIndex(250);

		container.add(widgetPanel);

		initWidgets();

	}

	private void initWidgets() {
		for (Entry<String, Provider<IDashboardWidget>> entry : widgetProvider.entrySet()) {
			String modelType = entry.getKey();
			ImageResource iconImage = entry.getValue().get().getSelectorIcon();
			widgetPanel.add(new PaletteWidget(iconImage, modelType));
		}
	}
}