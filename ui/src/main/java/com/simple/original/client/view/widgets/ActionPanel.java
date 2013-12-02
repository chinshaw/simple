package com.simple.original.client.view.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.resources.ResourcesFactory;

public class ActionPanel extends Composite {

    public static class ActionButton extends Composite implements ClickHandler {
        
        private SimplePanel container = new SimplePanel();
        
        private Resources resources = ResourcesFactory.getResources();
        
        public ActionButton(Image image) {
            initWidget(container);
            container.addDomHandler(this, ClickEvent.getType());
            container.setStyleName(resources.style().actionPanelButton());
        }

        @Override
        public void onClick(ClickEvent event) {
            
        }
    }
    
    private FlowPanel container = new FlowPanel();
    
    public ActionPanel() {
        initWidget(container);
    }
    
    
    public void addActionButton(ActionButton button) {
        
    }
    
    public void addActionButton(ActionButton button, int index) {
        
    }
    
    
}
