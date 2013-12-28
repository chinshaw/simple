package com.simple.original.client.dashboard.designer;

import static com.google.gwt.query.client.GQuery.$;
import gwtquery.plugins.draggable.client.events.DragEvent;
import gwtquery.plugins.draggable.client.events.DragEvent.DragEventHandler;
import gwtquery.plugins.draggable.client.gwt.DraggableWidget;
import gwtquery.plugins.droppable.client.events.DropEvent;
import gwtquery.plugins.droppable.client.events.DropEvent.DropEventHandler;
import gwtquery.plugins.droppable.client.events.OutDroppableEvent;
import gwtquery.plugins.droppable.client.events.OutDroppableEvent.OutDroppableEventHandler;
import gwtquery.plugins.droppable.client.events.OverDroppableEvent;
import gwtquery.plugins.droppable.client.events.OverDroppableEvent.OverDroppableEventHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.dashboard.IDashboardWidget;
import com.simple.original.client.dashboard.WidgetFactory;
import com.simple.original.client.dashboard.designer.WidgetPalettePanel.PaletteWidget;
import com.simple.original.client.dashboard.events.WidgetAddedEvent;
import com.simple.original.client.dashboard.model.ICompositeWidgetModel;
import com.simple.original.client.dashboard.model.IWidgetModel;
import com.simple.original.client.resources.Resources;

/**
 * All logic of the drag and drop operation is contained in this class. Original
 * Author
 * 
 * @author Julien Dramaix (julien.dramaix@gmail.com)
 * 
 *         Modified by
 * @author Chris Hinshaw (chris.hinshaw@gmail.com)
 */
public class SortableDragAndDropHandler implements DropEventHandler, OverDroppableEventHandler, OutDroppableEventHandler, DragEventHandler {

	private static final Logger logger = Logger.getLogger(SortableDragAndDropHandler.class.getName());

	private HandlerRegistration dragHandlerRegistration;
	private FlowPanel panel;
	private SimplePanel placeHolder;
	private int placeHolderIndex;
	private final WidgetFactory widgetFactory;
	private final WidgetModelFactory widgetModelFactory;
	private final EventBus eventBus;

	public SortableDragAndDropHandler(FlowPanel panel, WidgetFactory widgetFactory, WidgetModelFactory widgetModelFactory, EventBus eventBus) {
		this.panel = panel;
		this.widgetFactory = widgetFactory;
		this.widgetModelFactory = widgetModelFactory;
		this.eventBus = eventBus;
		placeHolderIndex = -1;
	}

	/**
	 * When draggable is dragging inside the panel, check if the place holder
	 * has to move
	 */
	public void onDrag(DragEvent event) {
		maybeMovePlaceHolder(event.getHelper());
	}

	/**
	 * On drop, insert the draggable at the place holder index, remove handler
	 * on the {@link DragEvent} of this draggable and remove the visual place
	 * holder
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void onDrop(DropEvent event) {
		DraggableWidget<?> draggable = event.getDraggableWidget();
		if (!(draggable instanceof PaletteWidget)) {
			throw new RuntimeException("Widget is not a valid type");
		}

		PaletteWidget paletteWidget = (PaletteWidget) draggable;
		
		IDashboardWidget widget = widgetFactory.createWidget(paletteWidget.getModelType());

		IWidgetModel model = widgetModelFactory.create(paletteWidget.getModelType());
		if (!(model instanceof ICompositeWidgetModel)) {
			throw new RuntimeException("Incorrect type of drop panel");
		}

		widget.setModel(model);

		logger.fine("adding widget to index " + placeHolderIndex);
		// If there is a problem we need to catch it and still reset the view.
		try {
			panel.insert(widget, placeHolderIndex);
			eventBus.fireEvent(new WidgetAddedEvent(widget));
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unable to add widget to drop panel", e);
		}
		reset();
	}

	/**
	 * When a draggable is out the panel, remove handler on the
	 * {@link DragEvent} of this draggable and remove the visual place holder
	 */
	public void onOutDroppable(OutDroppableEvent event) {
		reset();
	}

	/**
	 * When a draggable is being over the panel, listen on the {@link DragEvent}
	 * of the draggable and put a visaul place holder.
	 */
	public void onOverDroppable(OverDroppableEvent event) {
		DraggableWidget<?> draggable = event.getDraggableWidget();

		DivElement div = DOM.createDiv().cast();
		draggable.getDraggableOptions().setHelper(div);
		// create a place holder
		createPlaceHolder(draggable, panel.getWidgetIndex(draggable));
		// listen drag event when draggable is over the droppable
		dragHandlerRegistration = draggable.addDragHandler(this);
	}

	/**
	 * Create a visual place holder
	 * 
	 * @param draggable
	 * @param initialPosition
	 */
	private void createPlaceHolder(Widget draggable, int initialPosition) {
		placeHolder = new SimplePanel();
		placeHolder.addStyleName(Resources.INSTANCE.style().placeHolder());
		placeHolder.setHeight("" + $(draggable).height() + "px");
		placeHolder.setWidth("" + $(draggable).width() + "px");

		if (initialPosition != -1) {
			panel.insert(placeHolder, initialPosition);
			placeHolderIndex = initialPosition;
		}
	}

	/**
	 * Return the index before which we should insert the draggable if this one
	 * is dropped now
	 * 
	 * @param draggableHelper
	 * @return
	 */
	private int getBeforeInsertIndex(Element draggableHelper) {
		if (panel.getWidgetCount() == 0) {
			// no widget, the draggable should just be added to the panel
			return -1;
		}

		// compare absoluteTop of the draggable with absoluteTop od all widget
		// containing in the panel
		int draggableAbsoluteTop = draggableHelper.getAbsoluteTop();

		for (int i = 0; i < panel.getWidgetCount(); i++) {
			Widget w = panel.getWidget(i);
			int widgetAbsoluteTop = w.getElement().getAbsoluteTop();
			if (widgetAbsoluteTop > draggableAbsoluteTop) {
				return i;
			}
		}

		// the draggable should just be added at the end of the panel
		return -1;
	}

	/**
	 * Check if we have to move the place holder
	 * 
	 * @param draggableHelper
	 */
	private void maybeMovePlaceHolder(Element draggableHelper) {
		int beforeInsertIndex = getBeforeInsertIndex(draggableHelper);

		if (placeHolderIndex > 0 && beforeInsertIndex == placeHolderIndex) {
			// placeHolder must not move
			return;
		}

		if (beforeInsertIndex >= 0) {
			// move the place holder and keep its position
			panel.insert(placeHolder, beforeInsertIndex);
			placeHolderIndex = beforeInsertIndex;
		} else {
			// insert the place holder at the end
			panel.add(placeHolder);
			placeHolderIndex = panel.getWidgetCount() - 1;
		}
	}

	private void reset() {
		// don't listen drag event on the draggable
		if (dragHandlerRegistration != null) {
			dragHandlerRegistration.removeHandler();
		}
		if (placeHolder != null) {
			// remove the place holder
			placeHolder.removeFromParent();
		}

		placeHolder = null;
		dragHandlerRegistration = null;
		placeHolderIndex = -1;
	}
}