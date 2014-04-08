package com.simple.original.client.overlays;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class ConfigurableNavigationOverlay extends JavaScriptObject {

    protected ConfigurableNavigationOverlay() {
    }
    
	public final native JsArray<ConfigurableNavigationItemOverlay> getJsItems()  /*-{
    	return this.items;
	}-*/;
    
    public final List<ConfigurableNavigationItemOverlay> getItems() {
       JsArray<ConfigurableNavigationItemOverlay> jsItems = getJsItems();
       if (jsItems == null) {
           return null;
       }
       
       List<ConfigurableNavigationItemOverlay> items = new ArrayList<ConfigurableNavigationItemOverlay>(); 
       int itemCount = jsItems.length();
       for (int i = 0; i < itemCount; i++) {
           items.add(jsItems.get(i));
       }
       
       return items;
    }    
    
}
