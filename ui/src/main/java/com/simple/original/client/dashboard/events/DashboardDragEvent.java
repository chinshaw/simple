package com.simple.original.client.dashboard.events;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.simple.original.client.dashboard.WidgetType;

public class DashboardDragEvent extends JavaScriptObject {

	public enum DropAction {
		ADD,
		MOVE
	}
	
	protected DashboardDragEvent() {
	}	

	public final native String getDropId() /*-{
		return this.dropId;
	}-*/;
	
	public final native void setDropId(String dropId) /*-{
		this.dropId = dropId;
	}-*/;
	
	private final native String getDropActionString() /*-{
		return this.action;
	}-*/;
	
	private final native void setDropActionString(String action) /*-{
		this.action = action;
	}-*/;


	private final native String getWidgetTypeString() /*-{
		return this.dropType;
	}-*/;
	
	private final native void setWidgetTypeString(String dropType) /*-{
		this.dropType = dropType;
	}-*/;
	
	public final WidgetType getWidgetType() {
		String widgetType = getWidgetTypeString();
		if (widgetType == null) {
			throw new NullPointerException("Widget type must be configured for a drop action of add");
		}
		return WidgetType.valueOf(widgetType);
	}
	
	public final DropAction getDropAction() {
		String dropAction = getDropActionString();
		if (dropAction== null) {
			throw new NullPointerException("Drop action was not set correctly prior to throwing on drop event");
		}
		return DropAction.valueOf(dropAction);
	}
	
	public static final DashboardDragEvent create(String modelType) {
		DashboardDragEvent dropEvent = JavaScriptObject.createObject().cast();
		dropEvent.setDropActionString(DropAction.ADD.name());
		dropEvent.setWidgetTypeString(modelType);
		return dropEvent;
	}
	
	/*
	public static final DashboardDragEvent create(String widgetId) {
		DashboardDragEvent dropEvent = JavaScriptObject.createObject().cast();
		dropEvent.setDropActionString(DropAction.MOVE.name());
		dropEvent.setDropId(widgetId);
		return dropEvent;
	}
	*/
	
	
	public static final DashboardDragEvent fromJSON(String jsonStr) {
		DashboardDragEvent event = JsonUtils.safeEval(jsonStr).cast();
		return event;
	}
}