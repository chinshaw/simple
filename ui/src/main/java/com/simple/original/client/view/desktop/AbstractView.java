/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simple.original.client.view.desktop;

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IView;
import com.simple.original.client.view.widgets.ErrorPanel;

/**
 * 
 * @author chris
 */
public abstract class AbstractView extends Composite implements IView, RequiresResize {

    private final Resources resources;
    private final EventBus eventBus;

    public AbstractView() {
        this((Resources)GWT.create(Resources.class));
    }

    public AbstractView(Resources resources) {
        this(null, resources);
    }
    
    public AbstractView(EventBus eventBus, Resources resources) {
        this.eventBus = eventBus;
        this.resources = resources;
    }
    
    public void showError(HTML error) {
        ErrorPanel errorPanel = getErrorPanel();
        if (errorPanel != null) {
            errorPanel.showErrorMessage(error);
        }
    }
    
    public void showError(String errorMessage, Set<ConstraintViolation<?>> violations) {
		ErrorPanel errorPanel = getErrorPanel();
		if (errorPanel == null) {
			return;
		}
		
    	for (ConstraintViolation<?> violation : violations) {
			errorMessage += violation.getMessage() + "\n";
		}
		
    	errorPanel.showErrorMessage(errorMessage);
    }
    
    public void showError(String errorMessage, String correctiveAction) {
        ErrorPanel errorPanel = getErrorPanel();       
        if (errorPanel != null) {           
             errorPanel.showErrorMessage(errorMessage);
             errorPanel.setCorrectiveAction(correctiveAction);
        }
    }

    @Override
    public void showError(String error) {
        ErrorPanel errorPanel = getErrorPanel();       
        if (errorPanel != null) {        	
        	 errorPanel.showErrorMessage(error);
        }
    }

    @Override
    public void showError(Throwable exception) {
        ErrorPanel errorPanel = getErrorPanel();
        if (errorPanel != null) {
            showError(exception.getMessage());
        }
    }

    @Override
    public void showError(String error, Throwable exception) {
        ErrorPanel errorPanel = getErrorPanel();
        if (errorPanel != null) {
            showError(error + "\nCause: " + exception.getMessage());
        }
    }
    
    public EventBus getEventBus() {
        return eventBus;
    }

    public Resources getResources() {
        return resources;
    }

    protected abstract ErrorPanel getErrorPanel();

    @Override
    public void onResize() {
        Widget child = getWidget();
        if ((child != null) && (child instanceof RequiresResize)) {
            ((RequiresResize) child).onResize();
        }
    }

    /**
     * This will display a gwt RequestFactory Server failure to the user. Should
     * be sanitized so that the message is useful.
     */
    @Override
    public void showError(String error, ServerFailure failure) {
        ErrorPanel errorPanel = getErrorPanel();
        if (errorPanel != null) {
            errorPanel.showErrorMessage(error, failure.getExceptionType(), failure.getStackTraceString());
        }
        showError("Server Error: " + error + ", Caused by: " + failure.getMessage());
    }


    @Override
    public void showOverlayWidget(Widget overlayWidget) {
        ((HasWidgets)getWidget()).add(overlayWidget);
        
    }
    
    public void removeOverlayWidget(Widget overlayWidget) {
        ((HasWidgets)getWidget()).remove(overlayWidget);
    }
    
    
    public abstract void reset();

}
