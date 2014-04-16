package com.simple.original.client.view.widgets;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class ExpandablePanel extends Composite implements OpenHandler<DisclosurePanel>, CloseHandler<DisclosurePanel> {

	/**
	 * This is the uibinder and it will use the view.DefaultView.ui.xml
	 * template.
	 */
	
	DisclosurePanel disclosurePanel;
	Image iconState;

	private final ImageResource openImage;
	private final ImageResource closedImage;

	public ExpandablePanel(ImageResource openImage, ImageResource closedImage,
			String title) {
		
		this.openImage = openImage;
		this.closedImage = closedImage;

		
		disclosurePanel.addOpenHandler(this);
		disclosurePanel.addCloseHandler(this);
	}

	public void setContent(Widget w) {
		disclosurePanel.add(w);
	}

	@Override
	public void onClose(CloseEvent<DisclosurePanel> event) {
		iconState.setResource(openImage);

	}

	@Override
	public void onOpen(OpenEvent<DisclosurePanel> event) {
		iconState.setResource(closedImage);
	}
}
