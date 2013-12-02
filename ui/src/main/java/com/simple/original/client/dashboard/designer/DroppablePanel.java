package com.simple.original.client.dashboard.designer;

import gwtquery.plugins.droppable.client.gwt.DroppableWidget;

import java.util.Iterator;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
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
	
	private final WidgetFactory widgetFactory;

	public DroppablePanel(WidgetFactory widgetFactory) {
		this.widgetFactory = widgetFactory;
		init();
		initWidget(innerPanel);
		setupDrop();
	}

	private void init() {
		innerPanel = new FlowPanel();
		// innerPanel.addStyleName(Resources.INSTANCE.css().sortablePanel());

	}

	/**
	 * Register drop handler !
	 */
	private void setupDrop() {
		SortableDragAndDropHandler sortableHandler = new SortableDragAndDropHandler(innerPanel, widgetFactory);
		addDropHandler(sortableHandler);
		addOutDroppableHandler(sortableHandler);
		addOverDroppableHandler(sortableHandler);
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
