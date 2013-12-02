package com.simple.original.client.overlays;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.simple.original.client.dashboard.TableWidget.MetricRowProxy;
import com.simple.original.client.proxy.ViolationProxy;

public class MetricMatrixOverlay extends MetricOverlay {

    
    protected MetricMatrixOverlay() {
        
    }

    public final native void setName(String name) /*-{
       this.name = name;
    }-*/;


    public final native String getName() /*-{
        return this.name;
    }-*/;


    public final native List<ViolationProxy> getViolations() /*-{
        return this.violations;
    }-*/;


    public final native Integer getVersion() /*-{
        return this.version;
    }-*/;

    private final native JsArrayString getJSHeaders() /*-{
        return this.headers;
    }-*/;
    
    
    private final native JsArray<MetricRowOverlay> getJsRows() /*-{ 
        return this.rows;
    }-*/;

    public final native JsArray<ColumnOverlay> getValue() /*-{
        return this.columns;
    }-*/;
    
    
    public final List<String> getHeaders() {
        JsArrayString jsHeaders = getJSHeaders();
        if (jsHeaders == null) {
            return null;
        }
        
        List<String> headers = new ArrayList<String>();
        int headerCount = getJSHeaders().length();
        for (int i = 0; i < headerCount; i++) {
            headers.add(jsHeaders.get(i));
        }
        return headers;
    }
    
    public final List<MetricRowProxy> getRows() {
        JsArray<MetricRowOverlay> jsRows = getJsRows();
        if (jsRows == null) {
            return null;
        }
        
        List<MetricRowProxy> rows = new ArrayList<MetricRowProxy>();
        int rowCount = jsRows.length();
        for (int i = 0; i < rowCount; i++) {
            rows.add(jsRows.get(i));
        }
        
        return rows;
    }
}
