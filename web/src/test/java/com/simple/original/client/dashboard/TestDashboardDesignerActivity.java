package com.simple.original.client.dashboard;

import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.simple.original.client.dashboard.activity.DashboardDesignerActivity;
import com.simple.original.client.dashboard.designer.DroppablePanel;
import com.simple.original.client.dashboard.model.IGaugeWidgetModel;

public class TestDashboardDesignerActivity extends DashboardTestCase {

	
	
	@Test
	public void testAddGaugeWidget() {
		final IOCDashboardInjector injector = GWT
				.create(IOCDashboardInjector.class);
		//IDashboardDesignerView view = injector.dashboardDesignerView();
		DashboardDesignerActivity activity = injector.dashboardDesignerActivity();
		
		WidgetFactory widgetFactory = injector.widgetFactory();
		IGaugeWidgetModel model = injector.gaugeWidgetModel();
		IGaugeWidget gaugeWidget = widgetFactory.createWidget(model);
		
		DroppablePanel dropPanel = activity.getDisplay().getDroppbalePanel();
		dropPanel.add(gaugeWidget);
		
		
		/*
		NativeEvent clickEvent = Document.get().createClickEvent(0, 0, 0, 0, 0,
	            false, false, false, false);
		DomEvent.fireNativeEvent(clickEvent, gaugeWidget.asWidget());
		
		IWidgetEditor<?> modelEditor = activity.getDisplay().getWidgetPropertiesPanel().getPropertiesEditor();
		assertNotNull(modelEditor);
		*/
	}
	
	
	@Test
	public void testSelectGauge() {
		final IOCDashboardInjector injector = GWT
				.create(IOCDashboardInjector.class);
		
		//IDashboardDesignerView view = injector.dashboardDesignerView();
		DashboardDesignerActivity activity = injector.dashboardDesignerActivity();
		
		WidgetFactory widgetFactory = injector.widgetFactory();
		IGaugeWidgetModel model = injector.gaugeWidgetModel();
		IGaugeWidget gaugeWidget = widgetFactory.createWidget(model);
		
		DroppablePanel dropPanel = activity.getDisplay().getDroppbalePanel();
		dropPanel.add(gaugeWidget);
		NativeEvent clickEvent = Document.get().createClickEvent(0, 0, 0, 0, 0,
	            false, false, false, false);
		DomEvent.fireNativeEvent(clickEvent, gaugeWidget.asWidget());
		
		IWidgetEditor<?> modelEditor = activity.getDisplay().getWidgetPropertiesPanel().getPropertiesEditor();
		assertNotNull(modelEditor);
	}
	
}
