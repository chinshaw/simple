package com.simple.original.client.dashboard.designer;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.simple.original.client.dashboard.model.IWidgetModel;

public class WidgetModelFactory {

	/**
	 * This is the widgeModelProvider that is esstentially a hash map of the
	 * list of models that are available.
	 */
	private Map<String, Provider<IWidgetModel>> widgetModelProvider;
	
	@Inject
	public WidgetModelFactory(Map<String, Provider<IWidgetModel>> widgetModelProvider) {
		this.widgetModelProvider = widgetModelProvider;
	}
	
	public final <T extends IWidgetModel> T create(
			String iWidgetModelType) {
		T widget = (T) widgetModelProvider.get(iWidgetModelType).get();
		if (widget == null) {
			throw new RuntimeException("Unsupported widget type "
					+ iWidgetModelType);
		}
		return widget;
	}
}
