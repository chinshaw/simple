package com.simple.original.client.dashboard.model.jso;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.JavaScriptObject;
import com.simple.original.client.dashboard.model.IWidgetStyle;

public class WidgetStyleJso extends JavaScriptObject implements IWidgetStyle {

	protected WidgetStyleJso() {
	}

	@Override
	public final native int getHeight()  /*-{
		return this.height;
	}-*/;
	
	public final native int setHeight(int height) /*-{
		this.height = height;
	}-*/;

	@Override
	public final native int getWidth() /*-{
		return width;
	}-*/;
	
	public final native void setWidth(int width) /*-{
		this.width = width;
	}-*/;

	@Override
	public final native CssColor getBackgroundColor() /*-{
		return backgroundColor;
	}-*/;
	
	public final native void setBackgroundColor(CssColor backgroundColor) /*-{
		this.backgroundColor = backgroundColor;
	}-*/;
	
}
