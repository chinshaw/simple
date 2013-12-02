package com.simple.original.client.dashboard.designer;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.simple.original.client.dashboard.IWidgetModelEditor;

public class WidgetEditorFactory {
	
	
	private final Map<String, Provider<IWidgetModelEditor>> widgetEditorProvider;
	
	@Inject
	public WidgetEditorFactory(Map<String, Provider<IWidgetModelEditor>> widgetEditorProvider) {
		this.widgetEditorProvider = widgetEditorProvider;
	}
	
	public IWidgetModelEditor<?> create(String modelType) {
		
		IWidgetModelEditor<?> editor =  widgetEditorProvider.get(modelType).get();
		if (editor == null) {
			throw new RuntimeException("Unsupported widget type " + modelType);
		}
		return editor;
	}
}
