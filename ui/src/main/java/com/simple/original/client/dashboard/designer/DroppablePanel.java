package com.simple.original.client.dashboard.designer;

import gwtquery.plugins.droppable.client.gwt.DroppableWidget;

import java.util.Iterator;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.dashboard.WidgetFactory;

/**
 * Droppable Panel containing the portlets.
 * 
 * @author Julien Dramaix (julien.dramaix@gmail.com)
 * 
 */
public class DroppablePanel extends DroppableWidget<FlowPanel> implements
		HasWidgets {

	private FlowPanel innerPanel;
	
	private SortableDragAndDropHandler sortableHandler;
	
	public DroppablePanel(WidgetFactory widgetFactory, WidgetModelFactory widgetModelFactory, EventBus eventBus) {
		initWidget(innerPanel = new FlowPanel());
		sortableHandler = new SortableDragAndDropHandler(innerPanel, widgetFactory, widgetModelFactory, eventBus);
		setupDrop();
	}

	/**
	 * Register drop handler !
	 */
	private void setupDrop() {

		addDropHandler(sortableHandler);
		addOutDroppableHandler(sortableHandler);
		addOverDroppableHandler(sortableHandler);
		// Keeps it from allowing the drop to propagate
		getOptions().setGreedy(true);
	}

	@Override
	public void add(Widget w) {
		innerPanel.add(w);
	}
	
	public void add(IsWidget w) {
		innerPanel.add(w);
	}

	@Override
	public void clear() {
		innerPanel.clear();
	}

	@Override
	public Iterator<Widget> iterator() {
		return innerPanel.iterator();
	}

	@Override
	public boolean remove(Widget w) {
		return innerPanel.remove(w);
	}
	
	public int getWidgetCount() {
		return innerPanel.getWidgetCount();
	}

}
