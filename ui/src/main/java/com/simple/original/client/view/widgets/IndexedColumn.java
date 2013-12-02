package com.simple.original.client.view.widgets;

import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;
import com.simple.original.client.proxy.MetricDoubleProxy;
import com.simple.original.client.proxy.MetricProxy;
import com.simple.original.client.proxy.MetricStringProxy;

/**
 * This is to draw a dynamic celltable based on a range of values. Columns are
 * indexed by number.
 * 
 * @author chinshaw
 * 
 * @param <T>
 */
public class IndexedColumn<T extends MetricProxy> extends
		Column<List<T>, String> {
	int index;

	public IndexedColumn(int index) {
		super(new TextCell());
		this.index = index;
	}

	@Override
	public String getValue(List<T> list) {
		try {
			MetricProxy metric = list.get(index);
			if (metric instanceof MetricDoubleProxy) {
				return "" + ((MetricDoubleProxy) metric).getValue();
			}
			if (metric instanceof MetricStringProxy) {
				return ((MetricStringProxy) metric).getValue();
			}
		} catch (IndexOutOfBoundsException e) {

		}
		return "NA";
	}
}