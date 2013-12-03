package com.simple.original.client.dashboard;

import java.util.Map;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.multibindings.GinMapBinder;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.ResettableEventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.simple.original.client.dashboard.activity.DashboardActivity;
import com.simple.original.client.dashboard.activity.DashboardDesignerActivity;
import com.simple.original.client.dashboard.designer.DashboardDesignerView;
import com.simple.original.client.dashboard.designer.GaugeEditor;
import com.simple.original.client.dashboard.designer.IDashboardDesignerView;
import com.simple.original.client.dashboard.designer.PanelEditor;
import com.simple.original.client.dashboard.designer.PlotEditor;
import com.simple.original.client.dashboard.designer.TableEditor;
import com.simple.original.client.dashboard.designer.WidgetEditorFactory;
import com.simple.original.client.dashboard.designer.WidgetPalettePanel;
import com.simple.original.client.dashboard.model.IDashboardModel;
import com.simple.original.client.dashboard.model.IGaugeWidgetModel;
import com.simple.original.client.dashboard.model.IGaugeWidgetModel.IGaugeModelRange;
import com.simple.original.client.dashboard.model.IPanelWidgetModel;
import com.simple.original.client.dashboard.model.IPlotWidgetModel;
import com.simple.original.client.dashboard.model.ITableWidgetModel;
import com.simple.original.client.dashboard.model.jso.DashboardModelJso;
import com.simple.original.client.dashboard.model.jso.GaugeWidgetModelJso;
import com.simple.original.client.dashboard.model.jso.GaugeWidgetModelRangeJso;
import com.simple.original.client.dashboard.model.jso.PanelWidgetModelJso;
import com.simple.original.client.dashboard.model.jso.PlotWidgetModelJso;
import com.simple.original.client.dashboard.model.jso.TableWidgetModelJso;
import com.simple.original.client.place.PlaceController;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IDashboardView;

public class IOCTestDashboardModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(Resources.class).in(Singleton.class);
		bind(DashboardActivity.class);
		bind(DashboardDesignerActivity.class);
		
		bind(IGaugeWidget.class).to(MockGaugeWidget.class);
		bind(IDashboardView.class).to(DashboardView.class);
		bind(IDashboardDesignerView.class).to(DashboardDesignerView.class);
		
		bind(WidgetPalettePanel.class).in(Singleton.class);
		
		// Bind the types of widgets
		
		// This will map the model to their dashboard widget. We have to use
		// the name of the interface because of JavaScriptObject can't have 
		// instance methods otherwise we would simply a simple getter method.
		GinMapBinder<String, IDashboardWidget> mapBinder = GinMapBinder
				.newMapBinder(binder(), String.class, IDashboardWidget.class);
		
		mapBinder.addBinding(IGaugeWidgetModel.class.getName()).to(MockGaugeWidget.class);
		mapBinder.addBinding(IPlotWidgetModel.class.getName()).to(PlotWidget.class);
		mapBinder.addBinding(ITableWidgetModel.class.getName()).to(TableWidget.class);
		mapBinder.addBinding(IPanelWidgetModel.class.getName()).to(PanelWidget.class);
		
		
		// This will map the model to their editor widget. We have to use
		// the name of the interface because of JavaScriptObject can't have 
		// instance methods otherwise we would simply a simple getter method.
		GinMapBinder<String, IWidgetEditor> editorBinder = GinMapBinder
				.newMapBinder(binder(), String.class, IWidgetEditor.class);
		
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
	IDashboardModel dashboardModel() {
		return DashboardModelJso.create();
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

	@Provides
	@Singleton
	EventBus getEventBus() {
		return new ResettableEventBus(new SimpleEventBus());
	}
	
	@Provides
	@Inject
	@Singleton
	PlaceController placeController(EventBus eventBus) {
		return new PlaceController(eventBus);
	}
}
