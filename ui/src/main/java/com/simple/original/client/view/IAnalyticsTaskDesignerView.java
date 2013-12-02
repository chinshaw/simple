package com.simple.original.client.view;

import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.simple.original.client.proxy.AnalyticsOperationInputProxy;
import com.simple.original.client.proxy.AnalyticsOperationProxy;
import com.simple.original.client.proxy.AnalyticsTaskProxy;
import com.simple.original.client.proxy.DataProviderProxy;
import com.simple.original.client.proxy.DataProviderProxy.DataProviderType;
import com.simple.original.client.service.SearchRequestProvider;

public interface IAnalyticsTaskDesignerView extends IView, Editor<AnalyticsTaskProxy> {

    interface Presenter extends SearchRequestProvider<AnalyticsOperationProxy> {

        void onCancel();

        void onSaveChanges(String name, boolean isPublic);

        DataProviderProxy createDataProvider(DataProviderType type);

        Enum<?>[] getDataProviderTypes();

		void onEditOperation(AnalyticsOperationProxy value);

        Request<AnalyticsOperationProxy> fetchAnalyticsOperation(Long id);
        
        Request<List<AnalyticsOperationInputProxy>> getInputsForOperation(Long operationId);

		void onEditDashboard();
    }

    void setPresenter(Presenter presenter);

    /**
     * Get the driver used to edit analtyics tasks in the view.
     */
    RequestFactoryEditorDriver<? super AnalyticsTaskProxy, ?> getEditorDriver();

    /*
    void setSelectableDashboards(List<DashboardModelProxy> response);
	*/
    
	void setEnabledSaveButton(boolean value);

	void setEnabledCancelButton(boolean value);

    void scrollToTop();

    /**
     * Set the name of the dashboard for this task.
     * @param dashboardName
     */
	void setDashboardName(String dashboardName);
}
