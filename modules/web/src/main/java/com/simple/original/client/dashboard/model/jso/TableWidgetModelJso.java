package com.simple.original.client.dashboard.model.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.simple.original.client.dashboard.model.ITableWidgetModel;

public class TableWidgetModelJso extends WidgetModelJso implements ITableWidgetModel {

	protected TableWidgetModelJso() {
	}


	public static final TableWidgetModelJso create() {
		TableWidgetModelJso model = JavaScriptObject.createObject().cast();
		model.setType(ITableWidgetModel.class.getName());
		return model;
	}
}
