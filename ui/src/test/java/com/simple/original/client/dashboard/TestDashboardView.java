package com.simple.original.client.dashboard;

import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.simple.original.client.dashboard.model.IDashboardWidgetsModel;
import com.simple.original.client.dashboard.model.IGaugeWidgetModel;
import com.simple.original.client.dashboard.model.IPanelWidgetModel;
import com.simple.original.client.dashboard.model.IPlotWidgetModel;
import com.simple.original.client.dashboard.model.ITableWidgetModel;
import com.simple.original.client.view.IDashboardView;

/**
 * Test cases for the {@link DashboardView} class
 * 
 * @author chris
 * 
 */
public class TestDashboardView extends DashboardTestCase {

	public TestDashboardView() {
		// TODO Auto-generated constructor stub
	}

	public void testAddGauge() {
		final IOCDashboardInjector injector = GWT
				.create(IOCDashboardInjector.class);

		IDashboardView view = injector.dashboardView();

		IDashboardWidgetsModel model = injector.dashboardModel();

		assertNotNull(model);
		assertNotNull(model.getWidgets());
		IGaugeWidgetModel gaugeModel = injector.gaugeWidgetModel();

		model.addWidgetModel(gaugeModel);
		assertTrue(model.getWidgets().size() == 1);
		view.setModel(model);

		GWT.log("Number of widgets " + view.getDashboardWidgets().size());
		// assertTrue(view.getDashboardWidgets().size() == 1);
	}

	@Test
	public void testAddPlot() {
		final IOCDashboardInjector injector = GWT
				.create(IOCDashboardInjector.class);

		IDashboardView view = injector.dashboardView();

		IDashboardWidgetsModel model = injector.dashboardModel();

		assertNotNull(model);
		assertNotNull(model.getWidgets());
		IPlotWidgetModel gaugeModel = injector.plotWidgetModel();

		model.addWidgetModel(gaugeModel);
		assertTrue(model.getWidgets().size() == 1);
		view.setModel(model);

		GWT.log("Number of widgets " + view.getDashboardWidgets().size());
	}

	@Test
	public void testAddTable() {
		final IOCDashboardInjector injector = GWT
				.create(IOCDashboardInjector.class);

		IDashboardView view = injector.dashboardView();
		IDashboardWidgetsModel model = injector.dashboardModel();

		assertNotNull(model);
		assertNotNull(model.getWidgets());
		ITableWidgetModel gaugeModel = injector.tableWidgetModel();

		model.addWidgetModel(gaugeModel);
		assertTrue(model.getWidgets().size() == 1);
		view.setModel(model);

		GWT.log("Number of widgets " + view.getDashboardWidgets().size());
	}

	@Test
	public void testAddPanel() {
		final IOCDashboardInjector injector = GWT
				.create(IOCDashboardInjector.class);

		IDashboardView view = injector.dashboardView();
		IDashboardWidgetsModel model = injector.dashboardModel();

		assertNotNull(model);
		assertNotNull(model.getWidgets());
		IPanelWidgetModel panelModel = injector.panelWidgetModel();

		model.addWidgetModel(panelModel);
		assertTrue(model.getWidgets().size() == 1);
		view.setModel(model);
		
		GWT.log("Number of widgets " + view.getDashboardWidgets().size());
	}

	@Test
	public void testAddPanelWithGauge() {
		final IOCDashboardInjector injector = GWT
				.create(IOCDashboardInjector.class);

		IDashboardView view = injector.dashboardView();
		IDashboardWidgetsModel model = injector.dashboardModel();

		assertNotNull(model);
		assertNotNull(model.getWidgets());
		IPanelWidgetModel panelModel = injector.panelWidgetModel();

		panelModel.addWidgetModel(injector.gaugeWidgetModel());

		model.addWidgetModel(panelModel);
		assertTrue(model.getWidgets().size() == 1);
		view.setModel(model);
		
		PanelWidget p = (PanelWidget) view.getDashboardWidgets().get(0);
		
		assertTrue(p.getWidgetCount() == 1);
	}

	@Test
	public void testAddPanelWithTable() {
		final IOCDashboardInjector injector = GWT
				.create(IOCDashboardInjector.class);

		IDashboardView view = injector.dashboardView();
		IDashboardWidgetsModel model = injector.dashboardModel();

		assertNotNull(model);
		assertNotNull(model.getWidgets());
		IPanelWidgetModel panelModel = injector.panelWidgetModel();

		panelModel.addWidgetModel(injector.tableWidgetModel());

		model.addWidgetModel(panelModel);
		assertTrue(model.getWidgets().size() == 1);
		view.setModel(model);
		
		PanelWidget p = (PanelWidget) view.getDashboardWidgets().get(0);
		
		assertTrue(p.getWidgetCount() == 1);
	}
}
