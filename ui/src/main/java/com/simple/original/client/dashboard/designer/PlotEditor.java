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
import com.simple.original.client.dashboard.IWidgetModelEditor;
import com.simple.original.client.dashboard.events.WidgetModelChangedEvent;
import com.simple.original.client.dashboard.model.IPlotWidgetModel;
import com.simple.original.client.dashboard.model.jso.PlotWidgetModelJso;
import com.simple.original.client.proxy.AnalyticsOperationOutputProxy;

public class PlotEditor extends Composite implements IWidgetModelEditor<IPlotWidgetModel>  {

    /**
     * This is the uibinder and it will use the view.DefaultView.ui.xml
     * template.
     */
    public interface Binder extends UiBinder<Widget, PlotEditor> {
    }

    @UiField
    TextBox title;

    private PlotWidgetModelJso model;

    //@UiField(provided = true)
    //LinkableTaskPanel linkableTasks;

    @UiField(provided = true)
    ValueListBox<AnalyticsOperationOutputProxy> output = new ValueListBox<AnalyticsOperationOutputProxy>(new AbstractRenderer<AnalyticsOperationOutputProxy>() {

        @Override
        public String render(AnalyticsOperationOutputProxy metric) {
            return (metric == null) ? "Select..." : metric.getName();
        }
    });

    @Inject
    public PlotEditor(final EventBus eventBus) {
        //linkableTasks = new LinkableTaskPanel(eventBus, providesDashboards);
        initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

        title.addKeyUpHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(KeyUpEvent event) {
                model.setTitle(title.getValue());
                eventBus.fireEvent(new WidgetModelChangedEvent(model));
            }
        });

        /*
        List<AnalyticsOperationOutputProxy> availableOutputs = new ArrayList<AnalyticsOperationOutputProxy>();
        for (AnalyticsOperationOutputProxy possibleOutput : outputsProvider.getOutputs()) {
            if (possibleOutput.getOutputType() == IAnalyticsOperationOutput.Type.GRAPHIC) {
                availableOutputs.add(possibleOutput);
            }
        }

        output.setAcceptableValues(availableOutputs);

        output.addValueChangeHandler(new ValueChangeHandler<AnalyticsOperationOutputProxy>() {

            @Override
            public void onValueChange(ValueChangeEvent<AnalyticsOperationOutputProxy> event) {
                eventBus.fireEvent(new WidgetModelChangedEvent(model));
            }
        });
        */
    }

    public void setValue(PlotWidgetModelJso model) {
        this.model = model;
        this.title.setValue(model.getTitle());
    }

    @Override
    public IPlotWidgetModel getModel() {
        return model;
    }

}