package com.simple.original.client.dashboard.model;


public interface IProvidesWidgetModel<T extends IWidgetModel> {
	
	T getModel();
}
