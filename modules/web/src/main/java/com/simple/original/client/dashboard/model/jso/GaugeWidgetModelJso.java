package com.simple.original.client.dashboard.model.jso;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.simple.original.client.dashboard.model.IGaugeWidgetModel;

public class GaugeWidgetModelJso extends WidgetModelJso implements IGaugeWidgetModel {

	protected GaugeWidgetModelJso() {}

	@Override
	public final void setRanges(ArrayList<IGaugeModelRange> arrayList) {
	}

	@Override
	public final List<IGaugeModelRange> getRanges() {
		return null;
	}

	public static GaugeWidgetModelJso create() {
		GaugeWidgetModelJso model = JavaScriptObject.createObject().cast();
		model.setType(IGaugeWidgetModel.class.getName());
		return model;
	}
}
