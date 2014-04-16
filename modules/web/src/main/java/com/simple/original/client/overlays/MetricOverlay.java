package com.simple.original.client.overlays;

import com.google.gwt.core.client.JavaScriptObject;

public class MetricOverlay extends JavaScriptObject  {
    
    protected MetricOverlay() {
        
    }
    
    public  final native Long getMetricId() /*-{
        return this.id;
    }-*/;
}
