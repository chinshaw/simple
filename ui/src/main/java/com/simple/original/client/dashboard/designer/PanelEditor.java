package com.simple.original.client.dashboard.designer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.dashboard.IWidgetEditor;
import com.simple.original.client.dashboard.PanelWidget;
import com.simple.original.client.dashboard.events.WidgetRemoveEvent;
import com.simple.original.client.dashboard.model.IPanelWidgetModel;
import com.simple.original.client.dashboard.model.IWidgetModel;

public class PanelEditor extends Composite implements IWidgetEditor<PanelWidget>, WidgetRemoveEvent.Handler {


	/**
	 * This is the uibinder and it will use the view.DefaultView.ui.xml
	 * template.
	 */
	public interface Binder extends UiBinder<Widget, PanelEditor> {
	}

	@UiField
	TextBox title;

	private PanelWidget widget;

	@Inject
	public PanelEditor(final EventBus eventBus) {
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

		title.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				model().setTitle(title.getValue());
				widget.update();
			}
		});

		eventBus.addHandler(WidgetRemoveEvent.TYPE, this);
	}

	@Override
	public void onWidgetRemove(WidgetRemoveEvent event) {
		IWidgetModel widgetModel = event.getSelectedWidget().getModel();
		if (model().getWidgets().contains(widgetModel)) {
			model().getWidgets().remove(widgetModel);
		}
	}

	private IPanelWidgetModel model() {
		return widget.getModel();
	}
	
	private void update() {
		title.setValue(model().getTitle());
	}
	
	@Override
	public void setDashboardWidget(PanelWidget widget) {
		this.widget = widget;
		update();
		
	}

	@Override
	public PanelWidget getDashboardWidget() {
		return widget;
	}
}