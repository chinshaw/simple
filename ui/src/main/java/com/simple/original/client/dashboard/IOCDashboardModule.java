package com.simple.original.client.dashboard;

import java.util.Map;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.multibindings.GinMapBinder;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.simple.original.client.dashboard.designer.DashboardDesignerView;
import com.simple.original.client.dashboard.designer.GaugeEditor;
import com.simple.original.client.dashboard.designer.IDashboardDesignerView;
import com.simple.original.client.dashboard.designer.PanelEditor;
import com.simple.original.client.dashboard.designer.PlotEditor;
import com.simple.original.client.dashboard.designer.TableEditor;
import com.simple.original.client.dashboard.designer.WidgetEditorFactory;
import com.simple.original.client.dashboard.designer.WidgetPalettePanel;
import com.simple.original.client.dashboard.designer.WidgetPropertiesPanel;
import com.simple.original.client.dashboard.model.IDashboardWidgetsModel;
import com.simple.original.client.dashboard.model.IGaugeWidgetModel;
import com.simple.original.client.dashboard.model.IGaugeWidgetModel.IGaugeModelRange;
import com.simple.original.client.dashboard.model.IPanelWidgetModel;
import com.simple.original.client.dashboard.model.IPlotWidgetModel;
import com.simple.original.client.dashboard.model.ITableWidgetModel;
import com.simple.original.client.dashboard.model.jso.DashboardWidgetsModelJso;
import com.simple.original.client.dashboard.model.jso.GaugeWidgetModelJso;
import com.simple.original.client.dashboard.model.jso.GaugeWidgetModelRangeJso;
import com.simple.original.client.dashboard.model.jso.PanelWidgetModelJso;
import com.simple.original.client.dashboard.model.jso.PlotWidgetModelJso;
import com.simple.original.client.dashboard.model.jso.TableWidgetModelJso;
import com.simple.original.client.view.IDashboardView;

public class IOCDashboardModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(IDashboardView.class).to(DashboardView.class);
		bind(IDashboardDesignerView.class).to(DashboardDesignerView.class);

		bind(WidgetPalettePanel.class).in(Singleton.class);
		bind(WidgetPropertiesPanel.class).in(Singleton.class);

		// Bind the types of widgets

		// This will map the model to their dashboard widget. We have to use
		// the name of the interface because of JavaScriptObject can't have
		// instance methods otherwise we would simply a simple getter method.
		GinMapBinder<String, IDashboardWidget> widgetBinder = GinMapBinder.newMapBinder(binder(), String.class, IDashboardWidget.class);

		widgetBinder.addBinding(IGaugeWidgetModel.class.getName()).to(GaugeWidget.class);
		widgetBinder.addBinding(IPlotWidgetModel.class.getName()).to(PlotWidget.class);
		widgetBinder.addBinding(ITableWidgetModel.class.getName()).to(TableWidget.class);
		widgetBinder.addBinding(IPanelWidgetModel.class.getName()).to(PanelWidget.class);

		
		/*
		GinMapBinder<String, IWidgetModel> modelBinder = GinMapBinder.newMapBinder(binder(), String.class, IWidgetModel.class);
		modelBinder.addBinding(IDashboardWidgetsModel.class.getName()).to(DashboardWidgetsModelJso.class);
		modelBinder.addBinding(IWidgetModel.class.getName()).to(WidgetModelJso.class);
		modelBinder.addBinding(IGaugeWidgetModel.class.getName()).to(GaugeWidgetModelJso.class);
		modelBinder.add
		*/
		

		// This will map the model to their editor widget. We have to use
		// the name of the interface because of JavaScriptObject can't have
		// instance methods otherwise we would simply a simple getter method.
		GinMapBinder<String, IWidgetEditor> editorBinder = GinMapBinder.newMapBinder(binder(), String.class, IWidgetEditor.class);

		editorBinder.addBinding(IGaugeWidgetModel.class.getName()).to(GaugeEditor.class);
		editorBinder.addBinding(IPlotWidgetModel.class.getName()).to(PlotEditor.class);
		editorBinder.addBinding(ITableWidgetModel.class.getName()).to(TableEditor.class);
		editorBinder.addBinding(IPanelWidgetModel.class.getName()).to(PanelEditor.class);
	}

	@Inject
	@Provides
	@Singleton
	WidgetFactory widgetFactory(Map<String, Provider<IDashboardWidget>> widgetProvider) {
		return new WidgetFactory(widgetProvider);
	}

	@Inject
	@Provides
	@Singleton
	WidgetEditorFactory widgetEditorFactory(Map<String, Provider<IWidgetEditor>> widgetProvider) {
		return new WidgetEditorFactory(widgetProvider);
	}

	@Provides
	IDashboardWidgetsModel dashboardModel() {
		return DashboardWidgetsModelJso.create();
	}

	@Provides
	IGaugeWidgetModel gaugeModel() {
		return GaugeWidgetModelJso.create();
	}

	@Provides
	IGaugeModelRange gaugeModelRange() {
		return GaugeWidgetModelRangeJso.create();
	}

	@Provides
	IPlotWidgetModel plotModel() {
		return PlotWidgetModelJso.create();
	}

	@Provides
	ITableWidgetModel tableWidgetModel() {
		return TableWidgetModelJso.create();
	}

	@Provides
	IPanelWidgetModel panelWidgetModel() {
		return PanelWidgetModelJso.create();
	}
}
