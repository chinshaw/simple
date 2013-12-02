package com.simple.original.client.dashboard.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.simple.original.client.dashboard.IDashboardWidget;
import com.simple.original.client.dashboard.model.IContainsWidgets;

public class WidgetAddEvent extends GwtEvent<WidgetAddEvent.Handler> {

    public interface Handler extends EventHandler {

        void onWidgetAdd(WidgetAddEvent event);
    }

    public static final Type<Handler> TYPE = new Type<Handler>();

    private final IDashboardWidget<?> widget;

    private final IContainsWidgets parentContainer;

    public WidgetAddEvent(IContainsWidgets parentContainer, IDashboardWidget<?> widget) {
        this.parentContainer = parentContainer;
        this.widget = widget;
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    public IDashboardWidget<?> getCreatedWidget() {
        return widget;
    }

    public IContainsWidgets getContainsWidgets() {
        return parentContainer;
    }

    @Override
    protected void dispatch(Handler handler) {
        handler.onWidgetAdd(this);
    }
}