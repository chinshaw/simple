package com.simple.original.client.dashboard;

import java.util.Collection;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.simple.original.client.dashboard.model.IWidgetModel;

/**
 * This is used to generate dashboard widgets for the dashboard views. It is
 * used in the {@link DashboardView} to create widgets based upon their model.
 * It is also used extensively in the DashboardDesigner for multiple purposes
 * such as iterating over the types of widgets that are available. It's
 * constructor takes a map of the possible list of widgets with using a string
 * as key. By default we use the interface name as the key for the widget type.
 * This works so that we can also inject our own implementations at build time
 * inside {@link IOCDashboardModule}
 * 
 * 
 * @author chinshaw
 * 
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class WidgetFactory {

	private final Map<String, Provider<IDashboardWidget>> widgetProvider;

	@Inject
	public WidgetFactory(Map<String, Provider<IDashboardWidget>> widgetProvider) {
		this.widgetProvider = widgetProvider;
	}

	public final <T extends IDashboardWidget<? extends IWidgetModel>> T createWidget(
			String iWidgetModelType) {

		T widget = (T) widgetProvider.get(iWidgetModelType).get();
		if (widget == null) {
			throw new RuntimeException("Unsupported widget type "
					+ iWidgetModelType);
		}
		
		return widget;
	}

	public final <T extends IDashboardWidget> T createWidget(
			IWidgetModel model) {
		T widget = (T) createWidget(model.getType());
		if (widget == null) {
			throw new RuntimeException("Unsupported widget type");
		}

		widget.setModel(model);
		return widget;
	}

	public Collection<Provider<IDashboardWidget>> getRegisteredTypes() {
		return widgetProvider.values();
	}
}
