package com.simple.original.client.view.widgets;

import com.simple.original.client.dashboard.PanelWidget;

public interface HasParentPanel {

	 PanelWidget getParentPanel();
	
	 void setParentPanel(PanelWidget parentPanel);
}