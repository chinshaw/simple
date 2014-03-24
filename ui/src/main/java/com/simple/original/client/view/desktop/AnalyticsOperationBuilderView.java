/**
 * 
 */
package com.simple.original.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.HasEditorErrors;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.gwt.client.HasRequestContext;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.simple.api.exceptions.SimpleException;
import com.simple.api.orchestrator.IAnalyticsOperationOutput;
import com.simple.original.client.proxy.RAnalyticsOperationProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IOperationBuilderView;
import com.simple.original.client.view.widgets.CodeEditorPanel;
import com.simple.original.client.view.widgets.ErrorPanel;
import com.simple.original.client.view.widgets.ValueBoxEditorDecorator;

/**
 * 
 * 
 * @author chinshaw
 * 
 */
public class AnalyticsOperationBuilderView extends AbstractView implements IOperationBuilderView, Editor<RAnalyticsOperationProxy>, HasRequestContext<RAnalyticsOperationProxy> {

    public interface EditorDriver extends SimpleBeanEditorDriver<RAnalyticsOperationProxy, AnalyticsOperationBuilderView> {
    }

    /**
     * This is the uibinder and it will use the view.DefaultView.ui.xml
     * template.
     */
    @UiTemplate("AnalyticsOperationBuilderView.ui.xml")
    public interface Binder extends UiBinder<Widget, AnalyticsOperationBuilderView> {
    }

    // Future use.
    //@UiField
    //ListBox typeEditor = new ListBox();

    /**
     * Error panel to show errors when saving or editing analytics operation
     * component.
     */
    @UiField
    ErrorPanel errorPanel;
    

    @Path("publicAccessible")
	@UiField
	CheckBox isPublic;

    @UiField
    HTMLPanel contentPanel;
    
    
    @UiField
    DivElement propertiesBlock;
    
    @UiField
    DivElement inputsBlock;
    
    @UiField
    DivElement codeBlock;
    
    @UiField
    DivElement outputsBlock;
    
    
    @UiField
    Button propertiesNavigation;

    @UiField
    Button codeNavigation;

    @UiField
    Button inputsNavigation;

    @UiField
    Button outputsNavigation;
    
    
    

    /**
     * Name component for the name of the analytics operation.
     * This also implements {@link HasEditorErrors so it can show 
     * constraint violations when an error occurs.
     */
    @UiField
    ValueBoxEditorDecorator<String> name;

    /**
     * Description component that edits analytics operation description.
     * This also implements {@link HasEditorErrors} so it can show
     * constraint violations when an error occurs
     */
    @UiField
    ValueBoxEditorDecorator<String> description;

    /**
     * The outputs editor that will flush all the outputs back to the analytics
     * operation being edited.
     */
    @UiField
    @Path("outputs")
    public AnalyticsOperationOutputEditor outputsEditor;

    /**
     * Selectable listbox that will allow the user to add outputs to the view.
     */
    @Ignore
    @UiField
    public ListBox availableOutputTypes;
    
    @UiField
    CodeEditorPanel code;

    /**
     * Editor that will edit the selected data providers.
     */
    @UiField
    DataProviderInputsEditor dataProviders;

    @UiField
    InputsEditor inputsEditor;

    @Editor.Ignore
    @UiFactory
    AnalyticsOperationOutputEditor scriptOutputEditor() {
        return new AnalyticsOperationOutputEditor(getResources());
    }

    @UiHandler("addOutput")
    void addOutput(ClickEvent event) {
        onAddOutput();
    }

    @UiField
    Button save;

    @UiField
    Button cancel;

    @UiField
    SimpleLayoutPanel executionContainer;
    
    @UiField
    DockLayoutPanel container;
    
    @UiField
    SplitLayoutPanel splitLayoutPanel;
    
    
    private Presenter presenter = null;


    /**
     * We have to implement a ValueAwareEditor and maintain a reference to our
     * value object in order to manually flush the inputs before persistenting
     * the analytics operation object.
     */
    // private RAnalyticsOperationProxy analyticsOperation = null;

    private EditorDriver driver = GWT.create(EditorDriver.class);


	private RequestContext context;

    /**
     * @param eventBus
     * @param resources
     */
    @Inject
    public AnalyticsOperationBuilderView(EventBus eventBus, Resources resources) {
        super(eventBus, resources);
        initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

        driver.initialize(this);
        isPublic.setEnabled(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.simple.original.client.view.ViewImpl#getErrorPanel()
     */
    @Override
    public ErrorPanel getErrorPanel() {
        return errorPanel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.simple.original.client.view.ViewImpl#reset()
     */
    @Override
    public void reset() {
    	errorPanel.clear();
        name.clearErrors();
        description.clearErrors();
        code.setValue("");
        
    }

    private void bindToPresenter() {
        Enum<?>[] outputTypes = presenter.getAvailableOutputTypes();
        availableOutputTypes.clear();
        for (Enum<?> outputType : outputTypes) {
            availableOutputTypes.addItem(outputType.name());
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
        bindToPresenter();
    }

    public void onAddOutput() {
        IAnalyticsOperationOutput.Type type = IAnalyticsOperationOutput.Type.valueOf(availableOutputTypes.getValue(availableOutputTypes.getSelectedIndex()));
        
        try {
            outputsEditor.asEditor().getList().add(presenter.createOutput(type));
        } catch (SimpleException e) {
            showError(e.getMessage(), e);
        }
    }

    /**
     * Hook up the cancel handler in the ui. TODO This button can probably be
     * removed.
     * 
     * @param clickEvent
     */
    @UiHandler("cancel")
    void onCancel(ClickEvent clickEvent) {
        presenter.onCancelAnalytics();
    }

    /**
     * Hook up the save handler. TODO see if we can remove save and simply save
     * changes using on change listener.
     * 
     * @param clickEvent
     */
    @UiHandler("save")
    void onSaveAnalytics(ClickEvent clickEvent) {
        presenter.onSave();
    }
    
    /**
	 * @return the isPublic
	 */
    @Ignore
	public CheckBox getIsPublic() {
		return isPublic;
	}
    
    /**
	 * @return the name
	 */
    public SimpleBeanEditorDriver<RAnalyticsOperationProxy, ?> getEditorDriver() {
        return driver;
    }

	@Override
	public String getOperationName() {
		return name.getTitle();
	}
	
	
	@UiHandler({"propertiesNavigation", "codeNavigation", "inputsNavigation", "outputsNavigation"}) 
	void onNavChange(ClickEvent click) {
		propertiesBlock.getStyle().setDisplay(Display.NONE);
		codeBlock.getStyle().setDisplay(Display.NONE);
		inputsBlock.getStyle().setDisplay(Display.NONE);
		outputsBlock.getStyle().setDisplay(Display.NONE);
		
		if (click.getSource() == propertiesNavigation) {
			propertiesBlock.getStyle().setDisplay(Display.BLOCK);
		}
		
		if (click.getSource() == codeNavigation) {
			codeBlock.getStyle().setDisplay(Display.BLOCK);
		}
		
		if (click.getSource() == inputsNavigation) {
			inputsBlock.getStyle().setDisplay(Display.BLOCK);
		}
		
		if (click.getSource() == outputsNavigation) {
			outputsBlock.getStyle().setDisplay(Display.BLOCK);
		}
		
	}
	

	/**
	 * 
	 */
	@Override
	public void setRequestContext(RequestContext context) {
		this.context = context;
		// Have to set the request context on the sub editors.
		dataProviders.setRequestContext(context);
		inputsEditor.setRequestContext(context);
	}
	
	@UiHandler("test")
	void onTest(ClickEvent click) {
		presenter.onTest();
	}


	@Override
	public AcceptsOneWidget getExecutionContainer() {
		return executionContainer;
	}

	@Override
	public void setExecutionContainerSize(int size) {
		splitLayoutPanel.setWidgetSize(executionContainer, 400);
		splitLayoutPanel.animate(1000);
	}

	@Override
	public int getExecutionContainerSize() {
		return splitLayoutPanel.getWidgetSize(executionContainer).intValue();
	}
}