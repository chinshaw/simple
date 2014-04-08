package com.simple.original.client.view.widgets;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;

public class MinuteSelector extends Composite implements HasValue<Integer>, HasChangeHandlers {

	private final ListBox minuteSelector = new ListBox();
	
	public MinuteSelector() {
		initWidget(minuteSelector);
		
		int i;
		for (i = 0; i < 60; i++) {
			String minute = "";
			if (i < 10) {
				minute += "0";
			}
			minute += i;
			minuteSelector.addItem(minute);
		}
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Integer> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public Integer getValue() {
		return Integer.parseInt(minuteSelector.getValue(minuteSelector.getSelectedIndex()));
	}

	@Override
	public void setValue(Integer value) {
		minuteSelector.setSelectedIndex(value);
	}

	@Override
	public void setValue(Integer value, boolean fireEvents) {
		minuteSelector.setSelectedIndex(value);
	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return minuteSelector.addChangeHandler(handler);
	}
}
