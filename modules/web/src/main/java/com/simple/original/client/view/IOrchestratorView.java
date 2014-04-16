/**
 * 
 */
package com.simple.original.client.view;

import com.google.gwt.event.dom.client.HasClickHandlers;

/**
 * @author chris
 *
 */
public interface IOrchestratorView extends IView {

	String getQueryText();

	HasClickHandlers getQueryExecute();

}
