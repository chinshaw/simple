package com.simple.original.client.view;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

public interface IMasterLayoutPanel extends IsWidget {

    public AcceptsOneWidget getTopDisplay();

    public AcceptsOneWidget getCenterDisplay();
    
    public void setTopPanelSize(Double size);
}
