package com.simple.original.client.dashboard.designer;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.dashboard.IWidgetModelEditor;
import com.simple.original.client.resources.Resources;

public class WidgetPropertiesPanel extends Composite {

	private SimplePanel container = new SimplePanel();
	
	
	@Inject
	public WidgetPropertiesPanel(EventBus eventBus, Resources resources) {
		initWidget(container);
		setStylePrimaryName(resources.style().widgetPropertiesPanel());
	}
	
	public IWidgetModelEditor<?> getPropertiesEditor() {
		return (IWidgetModelEditor<?>) container.getWidget();
	}
	
	public void setPropertiesEditor(IWidgetModelEditor<?> editor) {
		GWT.log("Setting widget edit");
		container.setWidget(editor);
	}
}
