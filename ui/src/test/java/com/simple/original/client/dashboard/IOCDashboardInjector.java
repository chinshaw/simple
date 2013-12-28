package com.simple.original.client.dashboard;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.simple.original.client.dashboard.activity.DashboardActivity;
import com.simple.original.client.dashboard.activity.DashboardDesignerActivity;
import com.simple.original.client.dashboard.designer.IDashboardDesignerView;
import com.simple.original.client.dashboard.designer.WidgetPalettePanel;
import com.simple.original.client.dashboard.model.IDashboardWidgetsModel;
import com.simple.original.client.dashboard.model.IGaugeWidgetModel;
import com.simple.original.client.dashboard.model.IGaugeWidgetModel.IGaugeModelRange;
import com.simple.original.client.dashboard.model.IPanelWidgetModel;
import com.simple.original.client.dashboard.model.IPlotWidgetModel;
import com.simple.original.client.dashboard.model.ITableWidgetModel;
import com.simple.original.client.view.IDashboardView;

@GinModules({ IOCTestDashboardModule.class})
public interface IOCDashboardInjector extends Ginjector {
	
	DashboardActivity dashboardActivity();
	
	DashboardDesignerActivity dashboardDesignerActivity();
	
	IDashboardWidgetsModel dashboardModel();
	
	IGaugeWidgetModel gaugeWidgetModel();

	IGaugeModelRange gaugeModelRange();
	
	IPlotWidgetModel plotWidgetModel();

	ITableWidgetModel tableWidgetModel();
	
	IGaugeWidget gaugeWidget();

	IPanelWidgetModel panelWidgetModel();
	
	PlotWidget plotWidget();

	IDashboardView dashboardView();
	
	IDashboardDesignerView dashboardDesignerView();

	WidgetFactory widgetFactory();
	
	WidgetPalettePanel widgetSelectorPanel();
}
