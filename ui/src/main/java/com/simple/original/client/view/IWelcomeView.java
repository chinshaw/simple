/**
 * 
 */
package com.simple.original.client.view;


/**
 * @author chinshaw
 * 
 */
public interface IWelcomeView extends IView {

	interface Presenter {

		void onDashboardsSelected();

		void onDataProvidersSelected();

		void onOperationsSelected();

		void onExecuteTaskSelected();

		void onDocumentationSelected();

		void onHadoopSelected();
	}

	public void setPresenter(Presenter presenter);
}
