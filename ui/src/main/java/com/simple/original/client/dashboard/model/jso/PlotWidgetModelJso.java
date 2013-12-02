package com.simple.original.client.dashboard.model.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.simple.original.client.dashboard.model.IPlotWidgetModel;


public class PlotWidgetModelJso extends WidgetModelJso implements IPlotWidgetModel {

	protected PlotWidgetModelJso() {
		
	}
	
	public static final PlotWidgetModelJso create() {
		PlotWidgetModelJso model = JavaScriptObject.createObject().cast();
		model.setType(IPlotWidgetModel.class.getName());
		return model;
	}
}
