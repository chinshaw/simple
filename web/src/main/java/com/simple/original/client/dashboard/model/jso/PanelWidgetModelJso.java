package com.simple.original.client.dashboard.model.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.simple.original.client.dashboard.model.IPanelWidgetModel;

public class PanelWidgetModelJso extends CompositeWidgetModelJso implements
		IPanelWidgetModel {

	protected PanelWidgetModelJso() {
	}

	public static final PanelWidgetModelJso create() {
		PanelWidgetModelJso model = JavaScriptObject.createObject().cast();
		model.setType(IPanelWidgetModel.class.getName());
		JsArray<WidgetModelJso> array = JavaScriptObject.createArray().cast();
		model.setJsWidgets(array);
		return model;
	}

}
