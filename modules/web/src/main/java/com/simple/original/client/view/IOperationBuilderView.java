package com.simple.original.client.view;

import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.CheckBox;
import com.simple.api.exceptions.SimpleException;
import com.simple.orchestrator.api.IRHadoopOperationOutput;
import com.simple.original.client.proxy.AnalyticsOperationOutputProxy;
import com.simple.original.client.proxy.RAnalyticsOperationProxy;
import com.simple.original.client.view.widgets.ErrorPanel;

public interface IOperationBuilderView extends IView {

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

        AnalyticsOperationOutputProxy createOutput(IRHadoopOperationOutput.Type  type) throws SimpleException;

        Enum<?>[] getAvailableOutputTypes();

		void onSave();

		void onTest();
    }

    void setPresenter(Presenter presenter);

    SimpleBeanEditorDriver<RAnalyticsOperationProxy, ?> getEditorDriver();

    @Ignore
    public CheckBox getIsPublic();

    public String getOperationName();
    
    public ErrorPanel getErrorPanel();

    public AcceptsOneWidget getExecutionContainer();
    
    public void setExecutionContainerSize(int size);
    
    public int getExecutionContainerSize();

}
