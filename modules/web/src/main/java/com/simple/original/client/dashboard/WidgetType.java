package com.simple.original.client.dashboard;

import com.simple.original.client.dashboard.model.IGaugeWidgetModel;
import com.simple.original.client.dashboard.model.IPanelWidgetModel;
import com.simple.original.client.dashboard.model.IPlotWidgetModel;
import com.simple.original.client.dashboard.model.ITableWidgetModel;
import com.simple.original.client.dashboard.model.IWidgetModel;


public enum WidgetType {

    WidgetModelProxy(IWidgetModel.class),
    Panel(IPanelWidgetModel.class),
    Gauge(IGaugeWidgetModel.class),
    Table(ITableWidgetModel.class),
    StaticChart(IPlotWidgetModel.class);
    
    private Class<? extends IWidgetModel> modelType;
    WidgetType(Class<? extends IWidgetModel> modelType) {
        this.modelType = modelType;
    }
    
    
    public Class<? extends IWidgetModel> getModelClass() {
        return modelType;
    }
}
