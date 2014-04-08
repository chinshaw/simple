package com.simple.original.client.view.widgets;

import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.resources.ResourcesFactory;

public class ModalDialogBox extends DialogBox {

    private final FlowPanel container = new FlowPanel();
    private final FlowPanel contentContainer  = new FlowPanel();
    
    private Resources resources = ResourcesFactory.getResources();
	
	public ModalDialogBox() {
		setWidget(container);
		setStyleName(resources.style().modalDialogBox());
		//setAnimationEnabled(true);
		
        Button closeButton = new Button();
        closeButton.setStyleName(resources.style().modalCloseButton());
        closeButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                hide(true);
            }
        });

        container.add(closeButton);
        container.add(contentContainer);
	}

	@Override
	public void add(Widget w) {
		contentContainer.add(w);
	}

	@Override
	public void clear() {
		contentContainer.clear();
	}

	@Override
	public Iterator<Widget> iterator() {
		return contentContainer.iterator();
	}

	@Override
	public boolean remove(Widget w) {
		return contentContainer.remove(w);
	}
}
