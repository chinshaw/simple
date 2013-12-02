package com.simple.original.client.dashboard.model;


/**
 * This is the global model of the dashboard, it is used to marshal objects to and from
 * json to the IWidgetModel objects.
 * @author chinshaw
 */
public interface IDashboardModel extends ICompositeWidgetModel {
	
	void addWidgetModel(IWidgetModel widget);
	
	void addWidgetModel(int index, IWidgetModel widget);
	
}
