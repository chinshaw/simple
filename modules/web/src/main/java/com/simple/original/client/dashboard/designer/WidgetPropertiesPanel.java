package com.simple.original.client.dashboard.designer;



import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.dashboard.IWidgetEditor;
import com.simple.original.client.resources.Resources;

public class WidgetPropertiesPanel extends Composite {

	private SimplePanel container = new SimplePanel();
	
	
	@Inject
	public WidgetPropertiesPanel(EventBus eventBus, Resources resources) {
		initWidget(container);
		setStylePrimaryName(resources.style().widgetPropertiesPanel());
	}
	
	public IWidgetEditor<?> getPropertiesEditor() {
		return (IWidgetEditor<?>) container.getWidget();
	}
	
	public void setPropertiesEditor(IWidgetEditor<?> editor) {
		GWT.log("Setting widget edit");
		container.setWidget(editor);
	}
}
