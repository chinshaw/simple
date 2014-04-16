package com.simple.original.client.dashboard.designer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.dashboard.IWidgetEditor;
import com.simple.original.client.dashboard.TableWidget;
import com.simple.original.client.dashboard.model.ITableWidgetModel;
import com.simple.original.client.proxy.AnalyticsOperationOutputProxy;

public class TableEditor extends Composite implements IWidgetEditor<TableWidget> {

	/**
	 * This is the uibinder and it will use the view.DefaultView.ui.xml
	 * template.
	 */
	public interface Binder extends UiBinder<Widget, TableEditor> {
	}

	//@UiField(provided = true)
	//LinkableTaskPanel linkableTasks;

	@UiField
	TextBox title;
	
	private TableWidget widget;

	@UiField(provided = true)
	ValueListBox<AnalyticsOperationOutputProxy> output = new ValueListBox<AnalyticsOperationOutputProxy>(new AbstractRenderer<AnalyticsOperationOutputProxy>() {

		@Override
		public String render(AnalyticsOperationOutputProxy metric) {
			return (metric == null) ? "Select..." : metric.getName();
		}
	});

	@Inject
	public TableEditor(final EventBus eventBus) {
		//linkableTasks = new LinkableTaskPanel(eventBus, tasksProvider);
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

		title.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				model().setTitle(title.getValue());
			}
		});

		/*
		List<AnalyticsOperationOutputProxy> availableOutputs = new ArrayList<AnalyticsOperationOutputProxy>();
		for (AnalyticsOperationOutputProxy possibleOutput : outputProvider.getOutputs()) {
			if (possibleOutput.getOutputType() == IAnalyticsOperationOutput.Type.TWO_DIMENSIONAL) {
				availableOutputs.add(possibleOutput);
			}
		}

		output.setAcceptableValues(availableOutputs);

		output.addValueChangeHandler(new ValueChangeHandler<AnalyticsOperationOutputProxy>() {

			@Override
			public void onValueChange(ValueChangeEvent<AnalyticsOperationOutputProxy> event) {
				AnalyticsOperationOutputProxy output = event.getValue();
				model.setMetricId(output.getId());
				eventBus.fireEvent(new WidgetModelChangedEvent(model));
			}
		});
		*/

	}

	private void update() {
		this.title.setValue(model().getTitle());
	}

	public ITableWidgetModel model() {
		return widget.getModel();
	}

	@Override
	public void setDashboardWidget(TableWidget widget) {
		this.widget = widget;
		update();
	}

	@Override
	public TableWidget getDashboardWidget() {
		return widget;
	}
}
