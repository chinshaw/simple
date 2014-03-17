/**
 * 
 */
package com.simple.original.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.simple.original.client.view.IOrchestratorView;
import com.simple.original.client.view.widgets.ErrorPanel;

/**
 * @author chris
 *
 */
public class OrchestratorView extends AbstractView implements IOrchestratorView {

	
	/**
	 * This is the uibinder and it will use the view.DefaultView.ui.xml
	 * template.
	 */
	@UiTemplate("OrchestratorView.ui.xml")
	public interface Binder extends UiBinder<Widget, OrchestratorView> {
	}
	
	@UiField
	TextArea uiQuery;
	
	@UiField
	Button runQuery;
	
	@UiField
	FlowPanel queryOutput;
	
	public OrchestratorView() {
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
	}
	
	/* (non-Javadoc)
	 * @see com.simple.original.client.view.desktop.AbstractView#getErrorPanel()
	 */
	@Override
	protected ErrorPanel getErrorPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.simple.original.client.view.desktop.AbstractView#reset()
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public String getQueryText() {
		return uiQuery.getValue();
	}
	
	@Override
	public HasClickHandlers getQueryExecute() {
		return runQuery;
	}
	
	
	
	

}
