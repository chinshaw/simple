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
import com.simple.original.client.dashboard.IWidgetModelEditor;
import com.simple.original.client.dashboard.events.WidgetAddEvent;
import com.simple.original.client.dashboard.events.WidgetModelChangedEvent;
import com.simple.original.client.dashboard.events.WidgetRemoveEvent;
import com.simple.original.client.dashboard.model.IPanelWidgetModel;
import com.simple.original.client.dashboard.model.IWidgetModel;

public class PanelEditor extends Composite implements IWidgetModelEditor<IPanelWidgetModel>, WidgetRemoveEvent.Handler,
		WidgetAddEvent.Handler {


	/**
	 * This is the uibinder and it will use the view.DefaultView.ui.xml
	 * template.
	 */
	public interface Binder extends UiBinder<Widget, PanelEditor> {
	}

	@UiField
	TextBox title;

	private IPanelWidgetModel model;

	@Inject
	public PanelEditor(final EventBus eventBus) {
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

		title.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				model.setTitle(title.getValue());
				eventBus.fireEvent(new WidgetModelChangedEvent(model));
			}
		});

		eventBus.addHandler(WidgetAddEvent.TYPE, this);
		eventBus.addHandler(WidgetRemoveEvent.TYPE, this);
	}

	
	public void setModel(IPanelWidgetModel value) {
		this.model = value;
		this.title.setValue(value.getTitle());
	}

	@Override
	public IPanelWidgetModel getModel() {
		return model;
	}

	@Override
	public void onWidgetRemove(WidgetRemoveEvent event) {
		IWidgetModel widgetModel = event.getSelectedWidget().getModel();
		if (model.getWidgets().contains(widgetModel)) {
			model.getWidgets().remove(widgetModel);
		}
	}

	@Override
	public void onWidgetAdd(WidgetAddEvent event) {
		if (event.getContainsWidgets() != null) {
			//IWidgetModel widgetModel = (IWidgetModel) event.getParentPanel();
			if (this.model == event.getContainsWidgets()) {
				model.getWidgets().add(event.getCreatedWidget().getModel());
			}
		}
	}
}