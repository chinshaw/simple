package com.simple.original.client.dashboard.model.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.simple.original.client.dashboard.model.IWidgetModel;

public class WidgetModelJso extends JavaScriptObject implements IWidgetModel {

	protected WidgetModelJso() {}
	
	
	@Override
	public final native String getDescription() /*-{
		return this.description;
	}-*/;
	
	public final native void setDescription(String description) /*-{
		this.description = description;
	}-*/;
	
	@Override
	public final native String getTitle() /*-{
		return this.title;
	}-*/;
	
	public final native void setTitle(String title) /*-{
		this.title = title;
	}-*/;

	@Override
	public final native Long getMetricId() /*-{
		this.metricId = metricId;
	}-*/;

	@Override
	public final native void setMetricId(Long metricId) /*-{
		this.metricId = metricId;
	}-*/;

	
	@Override
	public final native WidgetStyleJso getStyle() /*-{
		return this.style;
	}-*/;
	
	public final native void setStyle(WidgetStyleJso style) /*-{
		this.style = style;
	}-*/;
	
	public final native String toJson() /*-{
		return this.toString();
	}-*/;
	
	public final native String getType() /*-{
		return this.type;
	}-*/;
	
	protected final native void setType(String type) /*-{
		this.type = type;
	}-*/;
	

	@Override
	public final String getWidgetType() {
		return "GAUGE";
	}

}
