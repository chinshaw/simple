package com.simple.original.client.overlays;

import com.google.gwt.core.client.JavaScriptObject;
import com.simple.original.client.proxy.MetricMatrixProxy;

public class ColumnOverlay extends JavaScriptObject implements MetricMatrixProxy.ColumnProxy {

    protected ColumnOverlay() {
        
    }
    
    
    public  final native String getValue()  /*-{
        return this.rows;
    }-*/;


    @Override
    public final native String getHeader()  /*-{ 
        return this.header;
    }-*/;
}
