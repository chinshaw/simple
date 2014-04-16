package com.simple.original.client.dashboard.model.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.simple.original.client.dashboard.model.IDashboardWidgetsModel;

public class DashboardWidgetsModelJso extends CompositeWidgetModelJso implements
		IDashboardWidgetsModel {

	
	protected DashboardWidgetsModelJso() {
	}
	
	public static final DashboardWidgetsModelJso create() {
		DashboardWidgetsModelJso model = JavaScriptObject.createObject().cast();
		JsArray<WidgetModelJso> array = JavaScriptObject.createArray().cast();
		model.setJsWidgets(array);
		return model;
	}

	public static final IDashboardWidgetsModel fromJson(String json) {
		DashboardWidgetsModelJso jsoModel = JsonUtils.safeEval(json);
		return jsoModel;
	}
	

}
