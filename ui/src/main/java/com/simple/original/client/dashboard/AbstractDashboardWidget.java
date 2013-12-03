package com.simple.original.client.dashboard;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.dashboard.events.WidgetSelectedEvent;
import com.simple.original.client.dashboard.model.IWidgetModel;
import com.simple.original.client.events.HandlerCollection;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.widgets.Tooltip;
import com.simple.original.client.view.widgets.Tooltip.TooltipPosition;

public abstract class AbstractDashboardWidget<M extends IWidgetModel> extends
		Composite implements IDashboardWidget<M> {

	protected ClickHandler widgetSelectedHandler = new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
			event.stopPropagation();
			selectWidget();
		}
	};


	protected M model;
	protected final EventBus eventBus;
	protected final Resources resources;

	protected HandlerCollection handlers = new HandlerCollection();

	@Inject
	public AbstractDashboardWidget(EventBus eventBus, Resources resources) {
		this.eventBus = eventBus;
		this.resources = resources;
		addDomHandler(widgetSelectedHandler, ClickEvent.getType());
	//	addStyleName(resources.style().abstractDashboardWidget());
	}

	/**
	 * Getter for the model
	 */
	public M getModel() {
		return model;
	}

	/**
	 * Setter for the model
	 * 
	 * @param model
	 */
	public void setModel(M model) {
		this.model = model;
	}

	public abstract Widget asWidget();

	
	protected void selectWidget() {
		eventBus.fireEvent(new WidgetSelectedEvent(this));
	}

	protected void setTooltip(String tooltip) {
		if (tooltip != null && !tooltip.isEmpty()) {
			Tooltip tt = new Tooltip();
			tt.setText(model.getDescription());
			tt.setPosition(TooltipPosition.CURSOR);
			tt.attachTo(this);
		}
	}
}