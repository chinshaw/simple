/**
 * 
 */
package com.simple.original.client.view.desktop;

import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IWelcomeView;
import com.simple.original.client.view.widgets.ErrorPanel;

/**
 * @author chinshaw
 *
 */
public class WelcomeView extends AbstractView implements IWelcomeView {

	
	/**
	 * @param resources
	 */
	public WelcomeView(Resources resources) {
		super(resources);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param eventBus
	 * @param resources
	 */
	@Inject
	public WelcomeView(EventBus eventBus, Resources resources) {
		super(eventBus, resources);
		initWidget(new Label( "Place Holder For Welcome"));
	}

	/* (non-Javadoc)
	 * @see com.simple.original.client.view.desktop.ViewImpl#getErrorPanel()
	 */
	@Override
	protected ErrorPanel getErrorPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.simple.original.client.view.desktop.ViewImpl#reset()
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPresenter(Presenter presenter) {
		// TODO Auto-generated method stub
		
	}

}
