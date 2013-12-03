package com.simple.original.client.dashboard.designer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.dashboard.GaugeWidget;
import com.simple.original.client.dashboard.IGaugeWidget;
import com.simple.original.client.dashboard.IWidgetEditor;
import com.simple.original.client.dashboard.model.IGaugeWidgetModel;
import com.simple.original.client.proxy.AnalyticsOperationOutputProxy;

public class GaugeEditor extends Composite implements IWidgetEditor<GaugeWidget> {

    /**
     * This is the uibinder and it will use the view.DefaultView.ui.xml
     * template.
     */
    public interface Binder extends UiBinder<Widget, GaugeEditor> {
    }

    @UiField
    TextBox title;

    @UiField
    TextBox description;

    private GaugeWidget widget;

    //@UiField(provided = true)
    //LinkableTaskPanel linkableTasks;

    @UiField(provided = true)
    ValueListBox<AnalyticsOperationOutputProxy> output = new ValueListBox<AnalyticsOperationOutputProxy>(new AbstractRenderer<AnalyticsOperationOutputProxy>() {

        @Override
        public String render(AnalyticsOperationOutputProxy metric) {
            return (metric == null) ? "Select..." : metric.getName();
        }
    });

    @UiField(provided = true)
    GaugeModelRangesEditor rangesEditor;

    @Inject
    public GaugeEditor(final EventBus eventBus) {
        rangesEditor = new GaugeModelRangesEditor(eventBus);
        //linkableTasks = new LinkableTaskPanel(eventBus, tasksProvider);

        initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

        title.addKeyUpHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(KeyUpEvent event) {
                model().setTitle(title.getValue());
            }
        });

        output.addValueChangeHandler(new ValueChangeHandler<AnalyticsOperationOutputProxy>() {

            @Override
            public void onValueChange(ValueChangeEvent<AnalyticsOperationOutputProxy> event) {
                AnalyticsOperationOutputProxy output = event.getValue();
              //  model.setOutput(output);
                /*
                MetricDoubleProxy metricDouble = (MetricDoubleProxy) event.getValue();
                model.setMetric(metricDouble);
                rangesEditor.setRanges(metricDouble.getRanges());
                */
            }
        });

        /*
        List<AnalyticsOperationOutputProxy> availableOutputs = new ArrayList<AnalyticsOperationOutputProxy>();
        for (AnalyticsOperationOutputProxy possibleOutput : outputProvider.getOutputs()) {
            if (possibleOutput.getOutputType() == IAnalyticsOperationOutput.Type.NUMERIC) {
                availableOutputs.add(possibleOutput);
            }
        }

        output.setAcceptableValues(availableOutputs);
        */
    }

 
    private IGaugeWidgetModel model() {
        return widget.getModel();
    }
    
    private void update() {
        this.title.setValue(model().getTitle()); 
    }


	@Override
	public void setDashboardWidget(GaugeWidget widget) {
		this.widget = widget;
		update();
	}


	@Override
	public GaugeWidget getDashboardWidget() {
		return this.widget;
	}
}