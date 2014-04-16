package com.simple.original.client.dashboard;

import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.simple.original.client.dashboard.model.IGaugeWidgetModel;

public class TestDashboardWidgets extends DashboardTestCase {


	@Test
	public void testGaugeNoValue() {
		final IOCDashboardInjector injector = GWT.create(IOCDashboardInjector.class);
		
		IGaugeWidget widget = injector.gaugeWidget();
		IGaugeWidgetModel model = injector.gaugeWidgetModel();
		model.setTitle("Gauge Title");
		model.setDescription("Gauge Description");
		
		widget.setModel(model);
		widget.setValue(100);
		
		widget.setTitle("The title");
	}
}
