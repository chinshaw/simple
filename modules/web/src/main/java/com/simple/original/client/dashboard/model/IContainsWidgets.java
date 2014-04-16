package com.simple.original.client.dashboard.model;

import java.util.List;


public interface IContainsWidgets {
    
    public List<IWidgetModel> getWidgets();

    public void setWidgets(List<IWidgetModel> widgets);
    
    public void addWidgetModel(IWidgetModel widget);
    
    public void addWidgetModel(int index, IWidgetModel widget);
}
