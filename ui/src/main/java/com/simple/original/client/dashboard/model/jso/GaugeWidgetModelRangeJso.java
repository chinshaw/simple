package com.simple.original.client.dashboard.model.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.simple.original.client.dashboard.model.IGaugeWidgetModel.IGaugeModelRange;

public class GaugeWidgetModelRangeJso extends JavaScriptObject implements IGaugeModelRange {

	protected GaugeWidgetModelRangeJso() {}
	
	@Override
	public final native String getRangeName() /*-{
		return this.rangeName;
	}-*/;

	@Override
	public final native void setRangeName(String rangeName) /*-{
		this.rangeName = rangeName;
	}-*/;

	@Override
	public final native String getColor() /*-{
		return this.cololr;
	}-*/;

	@Override
	public final native void setColor(String color) /*-{
		this.color = color;
	}-*/;

	@Override
	public final native Double getMinimum() /*-{
		return this.minimum;
	}-*/;

	@Override
	public final native void setMinimum(Double min) /*-{
		this.min = min;
	}-*/;

	@Override
	public final native Double getMaximum()  /*-{
		return this.max;
	}-*/;

	@Override
	public final native void setMaximum(Double max) /*-{
		this.max = max;
	}-*/;

	public static GaugeWidgetModelRangeJso create() {
		return JavaScriptObject.createObject().cast();
	}
	
}
