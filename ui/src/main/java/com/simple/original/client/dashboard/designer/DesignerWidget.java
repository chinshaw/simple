package com.simple.original.client.dashboard.designer;

import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.dashboard.AbstractDashboardWidget;
import com.simple.original.client.dashboard.IDashboardWidget;
import com.simple.original.client.dashboard.IWidgetModelEditor;
import com.simple.original.client.dashboard.events.DashboardDragEvent;
import com.simple.original.client.dashboard.events.DesignerDragStartEvent;
import com.simple.original.client.dashboard.events.DesignerDragStartEvent.DesignerDragStartHandler;
import com.simple.original.client.dashboard.events.WidgetRemoveEvent;
import com.simple.original.client.dashboard.model.IWidgetModel;
import com.simple.original.client.events.HandlerCollection;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.resources.ResourcesFactory;

public abstract class DesignerWidget<T extends AbstractDashboardWidget<M>, M extends IWidgetModel, E extends IWidgetModelEditor<M>> implements IDashboardWidget<M>, DesignerDragStartHandler {

    private class ActionsPanel extends Composite {
        
        private FlowPanel container = new FlowPanel();
        
        private Image editProperties = new Image(resources.editProperties16());
        private Image delete = new Image(resources.delete16());
        
        public ActionsPanel() {
            initWidget(container);
            container.getElement().getStyle().setWidth(4, Unit.EM);
            resources.editProperties16();
            container.add(editProperties);
            container.add(delete);
        
            editProperties.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    onEditProperties(event);
                }
            });
            
            delete.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    onRemoveWidget(event);
                }
            });
        } 
    }
    
    
    private class DesignerWidgetWrapper extends Composite {
        
        private FlowPanel container = new FlowPanel();
        
        private final T widget;
        
        public DesignerWidgetWrapper(final T widget) {
            this.widget = widget;
            initWidget(container);
            container.add(new ActionsPanel());
            container.add(widget);
        }
        
        protected T getWrappedWidget() {
            return widget;
        }
    }
        
    private static final Logger logger = Logger.getLogger(DesignerWidget.class.getName());
    
    private String draggableId = HTMLPanel.createUniqueId();

    private final E editor;
    
    protected final EventBus eventBus;

    private Resources resources = ResourcesFactory.getResources();
    
    protected PopupPanel contextPopup;

    protected HandlerCollection handlers = new HandlerCollection();

    private final DesignerWidgetWrapper wrapper; 

    public DesignerWidget(EventBus eventBus, T dashboardWidget, E editor) {
        this.eventBus = eventBus;
        wrapper = new DesignerWidgetWrapper(dashboardWidget);
        this.editor = editor;

        contextPopup = new PopupPanel(true);
        // contextPopup.addStyleName(resources.style().popupContextMenu());
        contextPopup.hide();
    }
    
    protected void initContextHandlers() {
        if (wrapper.getWrappedWidget() == null) {
            logger.warning("dashboard widget was null when initializing handlers, NOT GOOD!!");
            return;
        }
        
        //final Element selectionElement = dashboardWidget.getContextSelectionArea();
        //final String selectionContextSelector = ResourcesFactory.getResources().style().selectionContext();
        
        /*
        handlers.add(dashboardWidget.asWidget().addDomHandler(this, ClickEvent.getType()));
        handlers.add(dashboardWidget.asWidget().addDomHandler(new MouseOverHandler() {
            
            @Override
            public void onMouseOver(MouseOverEvent event) {
                event.stopPropagation();
                event.preventDefault();
                selectionElement.addClassName(selectionContextSelector);
            }
        }, MouseOverEvent.getType()));
        handlers.add(dashboardWidget.asWidget().addDomHandler(new MouseOutHandler() {
            
            @Override
            public void onMouseOut(MouseOutEvent event) {
                event.stopPropagation();
                event.preventDefault();
                selectionElement.removeClassName(selectionContextSelector);
            }
        }, MouseOutEvent.getType()));
        */
    }

    protected void initDraggable() {
        wrapper.getWrappedWidget().getElement().setId(draggableId);
        wrapper.getWrappedWidget().getElement().setDraggable(Element.DRAGGABLE_TRUE);
        handlers.add(wrapper.getWrappedWidget().addDomHandler(this, DesignerDragStartEvent.getType()));
    }

    @Override
    public void onDragStart(DesignerDragStartEvent event) {
        event.stopPropagation();
        event.setDashboardWidget(wrapper.getWrappedWidget());

        DashboardDragEvent dropEvent = DashboardDragEvent.create(draggableId);
        String source = new JSONObject(dropEvent).toString();
        event.setData("text", source);
        event.getDataTransfer().setDragImage(wrapper.getWrappedWidget().getElement(), 10, 10);
    }

    
    /**
     * Called when a native click event is fired.
     * 
     * @param event the {@link ClickEvent} that was fired
     */
    private void onEditProperties(ClickEvent event) {
        event.preventDefault();
        event.stopPropagation();
        contextPopup.clear();
        contextPopup.setPopupPosition(event.getClientX(), event.getClientY());
        contextPopup.setWidget(editor.asWidget());
        contextPopup.show();
    }
    
    private void onRemoveWidget(ClickEvent click) {
        eventBus.fireEvent(new WidgetRemoveEvent(this));
    }

    public T getWrappedWidget() {
        return wrapper.getWrappedWidget();
    }
    
    public M getModel() {
        return wrapper.getWrappedWidget().getModel();
    }

    /**
     * Drag enter event handler.
     */
    public final void onDragEnter(DragEnterEvent event) {
        event.stopPropagation();
        event.preventDefault();
    }

    protected static IDashboardWidget<?> getWidget(com.google.gwt.user.client.Element element) {
        EventListener listener = DOM.getEventListener((com.google.gwt.user.client.Element) element);
        // No listener attached to the element, so no widget exist for this
        // element
        if (listener == null) {
            return null;
        }
        if (listener instanceof AbstractDashboardWidget<?>) {
            // GWT uses the widget as event listener
            return (AbstractDashboardWidget<?>) listener;
        }
        return null;
    }
    
    /**
     * Clears all of the element's style names and sets it to the given style.
     * 
     * @param elem the element whose style is to be modified
     * @param styleName the new style name
     */
    protected void setStyleName(String styleName) {
        wrapper.setStyleName(styleName);
    }

    /**
     * Implementation from IsWidget
     */
    @Override
    public Widget asWidget() {
        return wrapper;
    }
}
