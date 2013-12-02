package com.simple.original.client.dashboard.model.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.simple.original.client.dashboard.model.IDashboardModel;

public class DashboardModelJso extends CompositeWidgetModelJso implements
		IDashboardModel {

	
	protected DashboardModelJso() {
	}

	
	public static final DashboardModelJso create() {
		DashboardModelJso model = JavaScriptObject.createObject().cast();
		JsArray<WidgetModelJso> array = JavaScriptObject.createArray().cast();
		model.setJsWidgets(array);
		return model;
	}

}
