package com.simple.original.client.dashboard;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.IsWidget;
import com.simple.original.client.dashboard.model.IWidgetModel;

public interface IDashboardWidget<M extends IWidgetModel> extends IsWidget {

    public M getModel();
    
    public void setModel(M model);
    
    public void setTitle(String title);
    
    public ImageResource getSelectorIcon();
}
