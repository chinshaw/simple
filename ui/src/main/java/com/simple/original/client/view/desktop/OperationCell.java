package com.simple.original.client.view.desktop;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.simple.original.client.proxy.AnalyticsOperationProxy;

public abstract class OperationCell extends AbstractCell<AnalyticsOperationProxy> {

	public OperationCell() {
		super("click");
	}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context, AnalyticsOperationProxy value, SafeHtmlBuilder sb) {
	    String description = (value.getDescription() != null) ? value.getDescription() : "No description set for operation";
	    
		sb.appendHtmlConstant("<div class=\" operationCell\">");
		sb.appendHtmlConstant("<h3>" + (context.getIndex() + 1) + ": -  " + value.getName() + "</h3>");
		sb.appendHtmlConstant("<label style=\"font-style:italic; padding-left: 2em;\" >" + description + "</label>");
		sb.appendHtmlConstant("</div>");
	}

	public void onBrowserEvent(Context context, Element parent, AnalyticsOperationProxy value, NativeEvent event, ValueUpdater<AnalyticsOperationProxy> valueUpdater) {
		
		super.onBrowserEvent(context, parent, value, event, valueUpdater);
		if ("click".equals(event.getType())) {
			EventTarget eventTarget = event.getEventTarget();
			if (!Element.is(eventTarget)) {
				return;
			}

			if (Element.as(eventTarget).getId().equalsIgnoreCase("remove")) {
				onRemoveCell(value);
			}
			
			if (Element.as(eventTarget).getId().equalsIgnoreCase("edit")) {
				onEditOperation(value);
			}
		}
	}
	
	public abstract void onRemoveCell(AnalyticsOperationProxy value);
	
	public abstract void onEditOperation(AnalyticsOperationProxy value);
}