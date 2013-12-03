package com.simple.original.client.dashboard.designer;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.simple.original.client.dashboard.IWidgetEditor;

public class WidgetEditorFactory {
	
	
	private final Map<String, Provider<IWidgetEditor>> widgetEditorProvider;
	
	@Inject
	public WidgetEditorFactory(Map<String, Provider<IWidgetEditor>> widgetEditorProvider) {
		this.widgetEditorProvider = widgetEditorProvider;
	}
	
	public IWidgetEditor<?> create(String modelType) {
		
		IWidgetEditor<?> editor =  widgetEditorProvider.get(modelType).get();
		if (editor == null) {
			throw new RuntimeException("Unsupported widget type " + modelType);
		}
		return editor;
	}
}
