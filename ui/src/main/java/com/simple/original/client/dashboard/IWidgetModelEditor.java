package com.simple.original.client.dashboard;

import com.google.gwt.user.client.ui.IsWidget;
import com.simple.original.client.dashboard.model.IProvidesWidgetModel;
import com.simple.original.client.dashboard.model.IWidgetModel;

public interface IWidgetModelEditor<T extends IWidgetModel> extends IsWidget, IProvidesWidgetModel<T>   {

}
