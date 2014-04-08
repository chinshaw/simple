package com.simple.original.client.view;

import com.simple.original.client.view.widgets.ErrorPanel;


public interface ILoginView extends IView {
	
	interface Presenter {
		
		void doAuth(String username, String password);
		
		void doPasswordReset();
	}
	
	
	void setPresenter(Presenter presenter);
	
	/**
     * get errorPanel
	 * @return
	 */
	ErrorPanel getErrorPanel();
	
	void clearFields();
	
	
}
