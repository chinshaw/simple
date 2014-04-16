package com.simple.original.client.view.widgets;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ProgressWidget extends SimplePanel {
    
    public interface Style extends CssResource {

        String overlayPanel();
        
        String overlayLabel();
    }

    private FlowPanel centerPanelWrapper = new FlowPanel();
    
    public ProgressWidget(Widget centerWidget, String text, Style style) {
        setStyleName(style.overlayPanel());
        setWidget(centerPanelWrapper);
        
        centerPanelWrapper.add(centerWidget);
        Label label = new Label(text);
        label.setStyleName(style.overlayLabel());
        
        centerPanelWrapper.add(new Label(text));
    }
}
