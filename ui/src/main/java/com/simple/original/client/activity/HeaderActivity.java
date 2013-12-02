package com.simple.original.client.activity;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.inject.Inject;
import com.simple.original.client.resources.Resources;

public class HeaderActivity implements Activity {

	
	private FlowPanel container = new FlowPanel();
	
	@Inject
	public HeaderActivity(EventBus eventBus, Resources resources) {
		container.setStyleName(resources.style().topHeader());
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(container);
	}
}
