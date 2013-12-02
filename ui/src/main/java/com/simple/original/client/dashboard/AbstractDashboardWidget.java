package com.simple.original.client.dashboard;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
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
			onWidgetSelected(event.getNativeEvent());
		}
	};

	private final PopupPanel contextPopup = new PopupPanel(true);;

	private final MenuBar popupMenuBar = new MenuBar(true);

	protected M model;
	protected final EventBus eventBus;
	protected final Resources resources;

	protected HandlerCollection handlers = new HandlerCollection();

	@Inject
	public AbstractDashboardWidget(EventBus eventBus, Resources resources) {
		this.eventBus = eventBus;
		this.resources = resources;

		contextPopup.addStyleName(resources.style().popupContextMenu());
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

	protected void onWidgetSelected(NativeEvent event) {
		GWT.log("GOT WIDGET SELECTED EVENT");
		/*
		if (model instanceof ILinkable) {
			popupMenuBar.clearItems();

			ILinkable linkable = (ILinkable) model;
			if (linkable.isLinkable()) {
				List<LinkableDashboardProxy> linkableTasks = linkable
						.getLinkableTasks();

				for (final LinkableDashboardProxy linkableTask : linkableTasks) {
					popupMenuBar.addItem(linkableTask.getContext(),
							new Command() {

								@Override
								public void execute() {
									eventBus.fireEvent(new DashboardNavigationEvent(
											linkableTask.getDashboardId()));
									contextPopup.hide();
								}
							});
				}

				contextPopup.setPopupPosition(event.getClientX(),
						event.getClientY());
				contextPopup.setWidget(popupMenuBar);
				contextPopup.show();
			}
		}
		*/
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