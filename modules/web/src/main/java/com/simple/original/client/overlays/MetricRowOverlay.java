package com.simple.original.client.overlays;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.simple.original.client.dashboard.TableWidget.MetricCellProxy;
import com.simple.original.client.dashboard.TableWidget.MetricRowProxy;

public class MetricRowOverlay extends JavaScriptObject implements MetricRowProxy {

    protected MetricRowOverlay() {
    }
    
    private final native JsArray<MetricCellOverlay> getJsCells()  /*-{
        return this.cells;
    }-*/;


    @Override
    public final List<MetricCellProxy> getCells() {
       JsArray<MetricCellOverlay> jsCells = getJsCells();
       if (jsCells == null) {
           return null;
       }
       
       List<MetricCellProxy> cells = new ArrayList<MetricCellProxy>(); 
       int cellCount = jsCells.length();
       for (int i = 0; i < cellCount; i++) {
           cells.add(jsCells.get(i));
       }
       
       return cells;
    }

}
