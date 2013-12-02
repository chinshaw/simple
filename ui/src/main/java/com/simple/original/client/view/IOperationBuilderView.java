package com.simple.original.client.view;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.simple.original.api.analytics.IAnalyticsOperationOutput;
import com.simple.original.api.exceptions.SimpleException;
import com.simple.original.client.proxy.AnalyticsOperationDataProviderProxy;
import com.simple.original.client.proxy.AnalyticsOperationOutputProxy;
import com.simple.original.client.proxy.AnalyticsOperationProxy.OperationType;
import com.simple.original.client.proxy.RAnalyticsOperationProxy;
import com.simple.original.client.view.widgets.ErrorPanel;

public interface IOperationBuilderView extends IView, Editor<RAnalyticsOperationProxy> {

    public enum AnalyticsTaskInputType {
        TEXT_INPUT("Text Input"), 
        DATE_INPUT("Date Input"), 
        COMPLEX_INPUT("Complex Input");
        
        private final String displayValue;
        
        AnalyticsTaskInputType(String displayValue) {
            this.displayValue = displayValue;
        }
        
        public String getDisplayValue() {
            return displayValue;
        }
    }
    
    public interface Presenter {

        void onCancelAnalytics();

        void onAnalyticsTypeUpdated(OperationType value);

        AnalyticsOperationOutputProxy createOutput(IAnalyticsOperationOutput.Type  type) throws SimpleException;

        Enum<?>[] getAvailableOutputTypes();

        AnalyticsOperationDataProviderProxy createDataProvider();

		void onSave(String name, boolean publicFlag);
    }

    void setPresenter(Presenter presenter);

    RequestFactoryEditorDriver<RAnalyticsOperationProxy, ?> getEditorDriver();

    @Ignore
    public CheckBox getIsPublic();

    @Ignore
    public String getOperationName();
    
    public ErrorPanel getErrorPanel();

    void scrollToTop();

}
