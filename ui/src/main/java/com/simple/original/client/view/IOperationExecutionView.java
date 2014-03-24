/**
 * 
 */
package com.simple.original.client.view;

import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.original.client.proxy.DataProviderInputProxy;

/**
 * @author chris
 *
 */
public interface IOperationExecutionView extends IView {

	interface Presenter {
		
		void onExecute();
	}

	void setPresenter(Presenter presenter);

	/**
	 * Add a {@link DataProviderInputProxy} to the view.
	 * @param dip
	 */
	void addDataProvider(final DataProvider dip);

	/**
	 * Add an operation to the view to be managed.
	 * @param aoip
	 */
	void addOperationInput(final AnalyticsOperationInput aoip);

	
}
