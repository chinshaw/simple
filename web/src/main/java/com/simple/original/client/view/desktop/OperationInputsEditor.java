package com.simple.original.client.view.desktop;

import java.util.Date;
import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.requestfactory.gwt.client.HasRequestContext;
import com.simple.original.client.proxy.AnalyticsOperationInputProxy;
import com.simple.original.client.proxy.UIComplexInputModelProxy;
import com.simple.original.client.proxy.UIDateInputModelProxy;
import com.simple.original.client.proxy.UIUserInputModelProxy;

public interface OperationInputsEditor extends ValueAwareEditor<List<AnalyticsOperationInputProxy>>, HasRequestContext<List<AnalyticsOperationInputProxy>> {

    /**
     * This will edit the default type of user input which is a simple string
     * input.
     * 
     * @author chinshaw
     */
    interface StringEditorDriver extends SimpleBeanEditorDriver<UIUserInputModelProxy, StringEditor> {
    }

    /**
     * Driver that will edit a UIDateInputModelProxy object.
     * 
     * @author chinshaw
     */
    interface DateEditorDriver extends SimpleBeanEditorDriver<UIDateInputModelProxy, DateEditor> {
    }

    /**
     * Driver that will edit a UIDateInputModelProxy object.
     * 
     * @author chinshaw
     */
    interface ComplexEditorDriver extends SimpleBeanEditorDriver<UIComplexInputModelProxy, ComplexEditor> {
    }
    
    public interface HasDriver<T> {
    	public SimpleBeanEditorDriver<T, ?> getDriver();
    }
    
    interface BaseEditor<T> extends Editor<T>, IsWidget, HasDriver<T> {
    	Editor<String> displayName();
    	
    	Editor<String> inputName();
    	
    	void setIndex(int index);
    }
    
    interface DateEditor extends BaseEditor<UIDateInputModelProxy> {
    	Editor<Date> value();
    }
    
    interface StringEditor extends BaseEditor<UIUserInputModelProxy> {
    	Editor<String> value();
    	Editor<List<String>> definedInputs();
    }
    
    interface ComplexEditor extends BaseEditor<UIComplexInputModelProxy> {
    	Editor<List<AnalyticsOperationInputProxy>> inputs();
    }
    
    public ComplexEditor createComplexEditor(int index);
    
    public StringEditor createStringEditor(int index);
    
    public DateEditor createDateEditor(int index);
}
