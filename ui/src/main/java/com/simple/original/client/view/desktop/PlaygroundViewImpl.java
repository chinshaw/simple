package com.simple.original.client.view.desktop;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.widgets.ErrorPanel;

public class PlaygroundViewImpl extends AbstractView {

    private FlowPanel panel = new FlowPanel();

    @Inject
    public PlaygroundViewImpl(Resources resources) {
        super(resources);
        initWidget(panel);
    }

    @Override
    protected ErrorPanel getErrorPanel() {
        return null;
    }

    @Override
    public void reset() {
    }
    
    public HasWidgets getPlayground() {
        return panel;
    }
}
