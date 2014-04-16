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
import com.simple.original.client.dashboard.PlotWidget;
import com.simple.original.client.dashboard.model.IPlotWidgetModel;
import com.simple.original.client.proxy.AnalyticsOperationOutputProxy;

public class PlotEditor extends Composite implements IWidgetEditor<PlotWidget>  {

    /**
     * This is the uibinder and it will use the view.DefaultView.ui.xml
     * template.
     */
    public interface Binder extends UiBinder<Widget, PlotEditor> {
    }

    @UiField
    TextBox title;
    
    private PlotWidget widget;

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
                model().setTitle(title.getValue());
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

    public IPlotWidgetModel model() {
        return widget.getModel();
    }

    private void update() {
    	this.title.setValue(model().getTitle());
    }
    
	@Override
	public void setDashboardWidget(PlotWidget widget) {
		this.widget = widget;
		update();
	}

	@Override
	public PlotWidget getDashboardWidget() {
		return widget;
	}

}