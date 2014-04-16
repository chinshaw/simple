package com.simple.original.client.overlays;

import com.google.gwt.core.client.JavaScriptObject;
import com.simple.original.client.dashboard.TableWidget.MetricCellProxy;

public class MetricCellOverlay extends JavaScriptObject implements MetricCellProxy {

    
    protected MetricCellOverlay() {
        
    }

    
    public final native Long getMetricId() /*-{
        return this.id;
    }-*/;
    
    
    
    @Override
    public final native String getValue() /*-{
        return this.value
    }-*/;
}
