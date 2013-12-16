/**
 * 
 */
package com.simple.original.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.HasEditorErrors;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.simple.original.api.analytics.IAnalyticsOperationOutput;
import com.simple.original.api.exceptions.SimpleException;
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
public class AnalyticsOperationBuilderView extends AbstractView implements IOperationBuilderView, Editor<RAnalyticsOperationProxy> {

    public interface EditorDriver extends RequestFactoryEditorDriver<RAnalyticsOperationProxy, AnalyticsOperationBuilderView> {
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
    

    @Path("public")
	@UiField
	CheckBox isPublic;

    @UiField
    HTMLPanel contentPanel;

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
    //@UiField
    //DataProviderOperationEditor dataProviders;

    @UiField
    InputsEditorWorking inputsEditor;

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

    private Presenter presenter = null;


    /**
     * We have to implement a ValueAwareEditor and maintain a reference to our
     * value object in order to manually flush the inputs before persistenting
     * the analytics operation object.
     */
    // private RAnalyticsOperationProxy analyticsOperation = null;

    private EditorDriver driver = GWT.create(EditorDriver.class);

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
        isPublic.setEnabled(false);
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
        presenter.onSave(name.asEditor().getValue(), isPublic.getValue());
    }
    
    @UiHandler("testScript")
    void onTestScript(ClickEvent clickEvent) {
    	presenter.onTestScript();
    }

    /**
	 * @return the isPublic
	 */
    @Ignore
	public CheckBox getIsPublic() {
		return isPublic;
	}

    @UiHandler("codeFullScreen")
    public void onCodeFullScreen(ClickEvent click) {
           code.onFullScreen();
    }
    
    /**
	 * @return the name
	 */
    public RequestFactoryEditorDriver<RAnalyticsOperationProxy, ?> getEditorDriver() {
        return driver;
    }

    @Override
    public void scrollToTop() {
        contentPanel.getElement().getParentElement().setScrollTop(0);
    }


	@Override
	public String getOperationName() {
		return name.getTitle();
	}
}