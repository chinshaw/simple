/**
 * 
 */
package com.simple.original.client.view;

import java.util.Set;

import com.simple.original.client.proxy.SqlConnectionProxy;

/**
 * @author chinshaw
 * 
 */
public interface IDataProvidersView extends IView {

	public interface Presenter {

		void onTestConnections(Set<SqlConnectionProxy> selectedSet);

		void onAddDataProvider();

	}

	void setPresenter(Presenter presenter);
}
