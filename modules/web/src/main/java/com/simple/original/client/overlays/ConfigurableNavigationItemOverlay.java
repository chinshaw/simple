package com.simple.original.client.overlays;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class ConfigurableNavigationItemOverlay extends JavaScriptObject {

    protected ConfigurableNavigationItemOverlay() {
    }

    public final native String getName() /*-{
        return this.name;
    }-*/;

    public final native void setName(String name) /*-{
    	this.name = name;
    }-*/;
    
    public final native String getPlace() /*-{
        return this.place;
    }-*/;

    public final native void setPlace(String place) /*-{
		this.place = place;
    }-*/;
	
    
    public final native JsArray<ConfigurableNavigationItemOverlay> getItems() /*-{
    	return this.items;
    }-*/;

    public final native void setItems(JsArray<ConfigurableNavigationItemOverlay> items) /*-{
		this.items = items;
	}-*/;
    
}
