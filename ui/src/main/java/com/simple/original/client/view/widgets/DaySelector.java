package com.simple.original.client.view.widgets;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;

public class DaySelector extends Composite implements HasChangeHandlers {

	private final ListBox minuteSelector = new ListBox();
	
	public DaySelector() {
		initWidget(minuteSelector);
		
		int i;
		for (i = 1; i <= 31; i++) {
			String minute = "";
			if (i < 10) {
				minute += "0";
			}
			minute += i;
			minuteSelector.addItem(minute);
		}
		
		minuteSelector.addItem("Last day of month", "L");
	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return minuteSelector.addChangeHandler(handler);
	}
	
	public String getValue() {
		return minuteSelector.getValue(minuteSelector.getSelectedIndex());
	}
}
