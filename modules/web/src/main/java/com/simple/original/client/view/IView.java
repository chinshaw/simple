/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simple.original.client.view;

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

/**
 * 
 * @author chris
 */
public interface IView extends IsWidget {

    public void showError(HTML html);

    public void showError(String error);

    public void showError(String error, Set<ConstraintViolation<?>> violations);

    public void showError(String error, String correctiveAction);

    public void showError(Throwable thr);

    public void showError(String error, Throwable thr);

    public void showError(String error, ServerFailure failure);

    public void reset();

    public void showOverlayWidget(Widget overlayWidget);

    public void removeOverlayWidget(Widget overlayWidget);

}
