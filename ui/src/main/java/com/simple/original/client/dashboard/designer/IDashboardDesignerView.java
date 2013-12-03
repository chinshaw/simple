/**
 * 
 */
package com.simple.original.client.dashboard.designer;

import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.simple.original.client.dashboard.IProvidesAnalyticsOperationOutputs;
import com.simple.original.client.dashboard.events.WidgetAddedEvent;
import com.simple.original.client.dashboard.events.WidgetRemoveEvent;
import com.simple.original.client.dashboard.model.IDashboardModel;
import com.simple.original.client.view.IView;

/**
 * @author chinshaw
 * 
 */
public interface IDashboardDesignerView extends IView, WidgetRemoveEvent.Handler {

    public interface Presenter extends IProvidesAnalyticsOperationOutputs {

        void onTaskChanged(Long taskId);

        void onCancelEdit();
    }

    public abstract void setPresenter(Presenter presenter);
    
    public abstract void setDashboardModel(IDashboardModel dashboard);
    
    /**
     * The droppable panel is the panel that contains all the dashboard widgets.
     * @return
     */
    public abstract DroppablePanel getDroppbalePanel();
    
    /**
     * This is the widget palette panel, it supports dragging widgets out of the panel
     * and dropping them on the droppable panel.
     * @return
     */
    public WidgetPalettePanel getWidgetPalettePanel();
    
    /**
     * The WidgetProperties panel is a panel that will be used used to change properties
     * of a dashboard widget.
     * @return
     */
    public WidgetPropertiesPanel getWidgetPropertiesPanel();

 	public abstract void showWidgetPropertiesPanel(boolean show);
}
