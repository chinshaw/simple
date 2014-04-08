/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simple.original.client.view.widgets;

import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;

/**
 * This class will grow to handle more complex error management such as
 * expanding to give more detail.
 * 
 * @author chinshaw
 */
public class ErrorPanel extends FlowPanel implements HasHTML {

    private HTML errorMessage = new HTML();
    private HTML correctiveAction = new HTML();
    private DisclosurePanel traceContainer = null;

    public ErrorPanel() {
        this.addStyleName("errorPanel");

        errorMessage.setStyleName("errorMessage");
        this.add(errorMessage);
        this.add(correctiveAction);
    }

    public void showErrorMessage(HTML message) {
        this.errorMessage.setHTML(message.getHTML());
        getElement().getStyle().setDisplay(Display.BLOCK);
    }
    
    public void showErrorMessage(String text) {
        Logger.getLogger("ErrorPanelLogger").info("Logging error " + text);
        text = text.replaceAll("(\r\n|\n)", "<br />");
        errorMessage.setHTML(text);
        getElement().getStyle().setDisplay(Display.BLOCK);
    }

    public void showErrorMessage(String text, String errorType) {
        showErrorMessage(text);
        // TODO do something with errorType.
    }

    public void showErrorMessage(String text, String errorType, String backTrace) {
        showErrorMessage(text, errorType);

        traceContainer = new DisclosurePanel("more information");
        traceContainer.setStyleName("moreInfoPanel");
        traceContainer.setAnimationEnabled(true);

        if (backTrace != null) {
            backTrace = backTrace.replaceAll("(\r\n|\n)", "<br />");
            HTML stackTrace = new HTML("<p>" + backTrace + "</p>");
            traceContainer.setContent(stackTrace);
            add(traceContainer);
        }
    }

    public void clear() {
        errorMessage.setText("");
        
        if (traceContainer != null) {
            this.remove(traceContainer);
        }
        this.getElement().getStyle().setDisplay(Display.NONE);
    }

    @Override
    public String getText() {
        return errorMessage.getText();
    }

    @Override
    public void setText(String text) {
        showErrorMessage(text);
    }

    @Override
    public String getHTML() {
        return errorMessage.getHTML();
    }

    @Override
    public void setHTML(String html) {
        errorMessage.setHTML(html);
    }

    public void setCorrectiveAction(String correctiveAction) {
        String action = "<b>Corrective Action:</b> " + correctiveAction;
        this.correctiveAction.setHTML(action);
    }
}
