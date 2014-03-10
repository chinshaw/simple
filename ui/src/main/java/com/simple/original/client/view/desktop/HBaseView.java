/**
 * 
 */
package com.simple.original.client.view.desktop;

import com.google.gwt.user.client.ui.Label;
import com.simple.original.client.view.IHBaseView;
import com.simple.original.client.view.widgets.ErrorPanel;

/**
 * @author chris
 *
 */
public class HBaseView extends AbstractView implements IHBaseView {

	
	public HBaseView() {
		initWidget(new Label("Place HOlder"));
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

}
