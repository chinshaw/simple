package com.simple.original.client.view.widgets;

import java.util.Iterator;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.DragEndEvent;
import com.google.gwt.event.dom.client.DragEndHandler;
import com.google.gwt.event.dom.client.DragEvent;
import com.google.gwt.event.dom.client.DragHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.simple.original.client.resources.Resources;

public class DraggablePanel extends Composite implements HasWidgets, DragStartHandler, DragHandler, DragOverHandler, DragEndHandler {

    /**
     * Set of characteristic interfaces supported by the {@link DialogBox}
     * caption.
     * 
     */
    public interface Caption extends HasHTML, DragStartHandler, DragEndHandler, IsWidget {
    }

    private static final Logger logger = Logger.getLogger("DraggablePanel");
    
    /**
     * Default implementation of Caption. This will be created as the header if
     * there isn't a header specified.
     */
    public static class CaptionImpl extends HTML implements Caption {

        public CaptionImpl() {
            super();
            setStyleName("Caption");
        }

        @Override
        public void onDragStart(DragStartEvent event) {

        }

        @Override
        public void onDragEnd(DragEndEvent event) {
           
        }
    }

    private final FlowPanel contentPanel = new FlowPanel();

    private CaptionImpl caption;
    
    int startX, startY = 0;
            

    public DraggablePanel(Resources resources) {
        initWidget(contentPanel);
        caption = new CaptionImpl();
        
        caption.setStyleName(resources.style().tabbedPanelLabel());
        
        add(caption);
        
        getElement().getStyle().setDisplay(Display.BLOCK);
        getElement().getStyle().setZIndex(99);
        
        caption.getElement().setDraggable(Element.DRAGGABLE_TRUE);
        
        
        caption.addDomHandler(this,DragStartEvent.getType());
        caption.addDomHandler(this,DragEvent.getType());
        RootPanel.get().addDomHandler(this, DragOverEvent.getType());
    }
    

    /**
     * Adds a child widget.
     * 
     * @param w the widget to be added
     * @throws UnsupportedOperationException if this method is not supported (most
     *           often this means that a specific overload must be called)
     */
    public void add(Widget w) {
        contentPanel.add(w);
    }

    /**
     * Removes all child widgets.
     */
    public void clear() {
        contentPanel.clear();
    }

    /**
     * Gets an iterator for the contained widgets. This iterator is required to
     * implement {@link Iterator#remove()}.
     */
    public Iterator<Widget> iterator() {
        return contentPanel.iterator();
    }

    /**
     * Removes a child widget.
     * 
     * @param w the widget to be removed
     * @return <code>true</code> if the widget was present
     */
    public boolean remove(Widget w) {
        return contentPanel.remove(w);
    }

    @Override
    public void onDragStart(DragStartEvent event) {
        startX = event.getNativeEvent().getScreenX();
        startY = event.getNativeEvent().getScreenY();
        event.setData("text", "");
    }
    
    
    public Caption getCaption() {
        return caption;
    }

    /*
    @Override
    public void onDrag(DragEvent event) {
        int absX = event.getNativeEvent().getScreenX();
        int absY = event.getNativeEvent().getScreenY();

        // if the mouse is off the screen to the left, right, or top, don't
        // move the dialog box. This would let users lose dialog boxes, which
        // would be bad for modal popups.
        //if (absX < clientLeft || absX >= windowWidth || absY < clientTop) {
        //  return;
        //}
        event.getNativeEvent().getClientX();
        DialogBox;
        
       // logger.info( "Y is " + event.getNativeEvent().);

    }
    */
    
    @Override
    public void onDrag(DragEvent event) {

        event.preventDefault();
        event.stopPropagation();
        
        //Widget w = (Widget) event.getSource();
        
        //logger.info("offset is " + w.getAbsoluteLeft());
        
        //logger.info("Event is " +event.getNativeEvent().getScreenX());
        
       // logger.info("Y is " +DOM.eventGetClientX((Event) event.getNativeEvent()));
        // if the mouse is off the screen to the left, right, or top, don't
        // move the dialog box. This would let users lose dialog boxes, which
        // would be bad for modal popups.
        //if (absX < clientLeft || absX >= windowWidth || absY < clientTop) {
         // return;
        //}
    
        //setPopupPosition(absX - dragStartX, absY - dragStartY);
    }

    @Override
    public void onDragOver(DragOverEvent event) {
        event.preventDefault();
        event.stopPropagation();
        
        int absX = event.getNativeEvent().getClientX();
        int absY = event.getNativeEvent().getClientY();
        
        logger.info("X is " + (absX + startX));
        logger.info("Y  is " + (absY + startY));
        
        //getElement().getStyle().setLeft((absX - startX), Unit.PX);
        //getElement().getStyle().setTop((absY - startY), Unit.PX);
        
        //logger.info("Position is " + absX);
        
    }
    

    @Override
    public void onDragEnd(DragEndEvent event) {
        event.stopPropagation();
        event.preventDefault();
    }
}