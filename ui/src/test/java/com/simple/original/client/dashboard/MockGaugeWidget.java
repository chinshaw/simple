package com.simple.original.client.dashboard;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.dashboard.events.WidgetModelChangedEvent;
import com.simple.original.client.dashboard.model.IGaugeWidgetModel;
import com.simple.original.client.resources.Resources;

/**
 * Purpose of this class is purely for testing, HtmlUnit does not support
 * canvas so there are issues with rendering the gauge. This is a stub that can be 
 * used instead to inject the widget type. 
 * @author chris
 *
 */
public class MockGaugeWidget extends AbstractDashboardWidget<IGaugeWidgetModel> implements IGaugeWidget {
	
	private String title;
	
	private IGaugeWidgetModel model;
	
	private Number value;
	
	private FlowPanel container = new FlowPanel();
	
	@Inject
	public MockGaugeWidget(EventBus eventBus, Resources resources) {
		super(eventBus, resources);
		initWidget(container);
		
		addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onWidgetSelected(event.getNativeEvent());
			}
			
		}, ClickEvent.getType());
	}

	@Override
	public IGaugeWidgetModel getModel() {
		return model;
	}

	@Override
	public void setModel(IGaugeWidgetModel model) {
		this.model = model;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public String getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onWidgetModelChanged(WidgetModelChangedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Number getValue() {
		return value;
	}

	@Override
	public void setValue(Number value) {
		this.value = value;
	}

	@Override
	public ImageResource getSelectorIcon() {
		return resources.gaugeWidgetSelector();
	}

}
