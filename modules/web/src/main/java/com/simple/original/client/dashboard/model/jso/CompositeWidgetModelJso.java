package com.simple.original.client.dashboard.model.jso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.core.client.JsArray;
import com.simple.original.client.dashboard.model.ICompositeWidgetModel;
import com.simple.original.client.dashboard.model.IWidgetModel;

public class CompositeWidgetModelJso extends WidgetModelJso implements ICompositeWidgetModel {
	
	protected CompositeWidgetModelJso() {}
	
	public final native JsArray<WidgetModelJso> getJsWidgets() /*-{
		return this.widgets;
	}-*/;

	protected final native void setJsWidgets(JsArray<WidgetModelJso> widgets) /*-{
		this.widgets = widgets;
	}-*/;

	@Override
	public final List<IWidgetModel> getWidgets() {
		JsArray<WidgetModelJso> jsWidgets = getJsWidgets();
		if (jsWidgets == null) {
			return null;
		}

		List<IWidgetModel> widgets = new ArrayList<IWidgetModel>();
		int widgetCount = jsWidgets.length();
		
		for (int i = 0; i < widgetCount; i++) {
			widgets.add(jsWidgets.get(i));
		}

		return Collections.unmodifiableList(widgets);
	}

	@Override
	public final void setWidgets(List<IWidgetModel> widgets) {
		for (IWidgetModel widget : widgets) {
			getJsWidgets().push((WidgetModelJso) widget);
		}
	}
	
	public final void addWidgetModel(IWidgetModel widget) {
		getJsWidgets().push((WidgetModelJso) widget);
	}
	
	public final void addWidgetModel(int index, IWidgetModel widget) {
		getJsWidgets().push((WidgetModelJso) widget);
	}
}
