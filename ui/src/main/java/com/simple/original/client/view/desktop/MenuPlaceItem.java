package com.simple.original.client.view.desktop;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.MenuItem;

public class MenuPlaceItem extends MenuItem {

	private String place;

	@UiConstructor
	public MenuPlaceItem(SafeHtml html) {
		super(html);	
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getPlace() {
		return this.place;
	}
}
