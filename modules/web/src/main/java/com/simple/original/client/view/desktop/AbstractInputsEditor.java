package com.simple.original.client.view.desktop;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.Composite;
import com.simple.original.client.proxy.AnalyticsOperationInputProxy;
import com.simple.original.client.proxy.UIComplexInputModelProxy;
import com.simple.original.client.proxy.UIDateInputModelProxy;
import com.simple.original.client.proxy.UIUserInputModelProxy;
import com.simple.original.client.resources.Resources;

public abstract class AbstractInputsEditor extends Composite implements OperationInputsEditor {

	private static final Logger logger = Logger.getLogger("AbstractInputsEditor");
	
	protected final Resources resources;
	
    protected List<AnalyticsOperationInputProxy> inputs;
	
	protected List<BaseEditor<? extends AnalyticsOperationInputProxy>> editors = new ArrayList<BaseEditor<? extends AnalyticsOperationInputProxy>>();
	
    /**
     * List of our operation inputs, this is used during flushing to get the
     * user modified inputs. This is requied to to GWT bug
     * http://code.google.com/p/google-web-toolkit/issues/detail?id=6719.
     * Polymorphism / inheritence doesn't work for subeditor's so we have to
     * maintain a driver for each input object.
     */
    //protected List<SimpleBeanEditorDriver<? extends AnalyticsOperationInputProxy, ?>> inputDrivers = new ArrayList<SimpleBeanEditorDriver<? extends AnalyticsOperationInputProxy, ?>>();
	
	public AbstractInputsEditor(Resources resources) {
		this.resources = resources;
	}
    
    
    /**
     * This is my implementation that fixes the gwt bug for polymorphic editors. We actually have to flush the
     * values from the driver's back out to the working copy of inputs.
     */
    @Override
    public void flush() {
        // Clear out the inputs before we assign the inputs from our view.
        this.inputs.clear();
        
        for (BaseEditor<? extends AnalyticsOperationInputProxy> editor : editors) {
        	SimpleBeanEditorDriver<? extends AnalyticsOperationInputProxy, ?> inputDriver = editor.getDriver();
        	AnalyticsOperationInputProxy inputProxy = inputDriver.flush();
        	this.inputs.add(inputProxy);
        }
        logger.info("Nuber of inputs is " + this.inputs.size());
        editors.clear();
    }

    
    /**
     * Edit the UIUserInputModelProxy
     * 
     * @param input
     */
    public StringEditor initStringEditor(UIUserInputModelProxy input) {
    	StringEditor editor = createStringEditor(editors.size());
    	editor.getDriver().edit(input);
        return editor;
    }
    
    /**
     * Edit input type of UIDateInputModelProxy.
     * 
     * @param input
     */
    public DateEditor initDateEditor(UIDateInputModelProxy input) {
        DateEditor editor = createDateEditor(editors.size());
        editor.getDriver().edit(input);
        return editor;
    }
    
    /**
     * Edit input type of UIComplexModelProxy.
     * 
     * @param input
     */
    public ComplexEditor initComplexEditor(UIComplexInputModelProxy input) {
        ComplexEditor editor = createComplexEditor(editors.size());
        if (input.getInputs() == null) {
            input.setInputs(new ArrayList<AnalyticsOperationInputProxy>());
        }
        editor.getDriver().edit(input);
        return editor;
    }
}