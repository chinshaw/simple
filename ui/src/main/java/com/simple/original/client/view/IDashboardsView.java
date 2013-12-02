/**
 * 
 */
package com.simple.original.client.view;

import com.google.gwt.view.client.HasData;
import com.simple.original.client.proxy.DashboardProxy;

/**
 * @author chinshaw
 *
 */
public interface IDashboardsView extends IView {

	
	interface Presenter {
		void onNewDashboard();
	}
	
	void setPresenter(Presenter presenter);
	
	HasData<DashboardProxy> getDashboardDataProvider();
}
