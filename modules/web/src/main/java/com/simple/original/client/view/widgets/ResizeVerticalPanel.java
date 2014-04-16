package com.simple.original.client.view.widgets;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ResizeVerticalPanel extends VerticalPanel implements RequiresResize, ProvidesResize {

	@Override
	public void onResize() {
		Window.alert("Calling resize");
		for (Widget child : getChildren()) {
			if (child instanceof RequiresResize) {
				((RequiresResize) child).onResize();
			}
		}
	}
}
