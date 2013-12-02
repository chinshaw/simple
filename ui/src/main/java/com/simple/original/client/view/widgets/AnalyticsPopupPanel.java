/**
 * 
 */
package com.simple.original.client.view.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * @author Iliyas
 *
 */
public class AnalyticsPopupPanel extends PopupPanel {
	
    /**
     * This is a widget container.
     */
    private FlowPanel container = new FlowPanel();   
    
    /**
     * Submit button
     */
    private Button submit = new Button("Ok"); 
    
    /**
     * Label that contains the message content.
     */
    private Label content;

	/**
	 * default constructor 
	 */
	public AnalyticsPopupPanel() {
	}

	/**
	 * @param autoHide
	 */
	public AnalyticsPopupPanel(boolean autoHide) {
		super(autoHide);
		setGlassEnabled(true);
		setVisible(true);
	}

	/**
	 * @param autoHide
	 * @param modal
	 */
	public AnalyticsPopupPanel(boolean autoHide, boolean modal) {
		super(autoHide, modal);
	}

	/**
	 * Shows the popup and attach it to the page. 
	 */
	public void showPopup() {		
	    submit.addClickHandler(new ClickHandler() {

	        @Override
	        public void onClick(ClickEvent event) {       
	        	setVisible(false);	        
	        	remove(container);
	        	clear();
	        	removeFromParent();
	        }
	    });
	    container.add(submit);
	    
	    this.add(container);
	    this.center();	
	}
	
	public void setMessage(String content) {
		this.content = new Label();		
		this.content.setText(content);
		addContent();
	}
	
	private void addContent() {
		container.add(this.content);
	}

}
