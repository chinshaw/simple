package com.simple.original.client.view.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.gwt.client.HasRequestContext;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.simple.original.client.proxy.SqlConnectionProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.widgets.ErrorPanel;
import com.simple.original.client.view.widgets.ValueBoxEditorDecorator;


public class SqlConnectionEditor extends AbstractView implements Editor<SqlConnectionProxy>, HasRequestContext<SqlConnectionProxy>  {


    /**
     * This is the uibinder and it will use the view.DefaultView.ui.xml
     * template.
     */
    @UiTemplate("SqlConnectionEditor.ui.xml")
    public interface Binder extends UiBinder<Widget, SqlConnectionEditor> {
    }

    
    public interface Driver extends RequestFactoryEditorDriver<SqlConnectionProxy, SqlConnectionEditor> {
    }
    
    private Driver driver;
    

    @UiField
    ValueBoxEditorDecorator<String> name;

    @UiField
    ValueBoxEditorDecorator<String> host;

    @UiField
    ValueBoxEditorDecorator<String> userName;

    @UiField
    ValueBoxEditorDecorator<String> password;

    @UiField
    Button clear;

    @UiField
    Button save;
    
    
    @Editor.Path("driverName")
    @UiField(provided = true)
    ValueListBox<String> availableDrivers = new ValueListBox<String>(new AbstractRenderer<String>() {

        @Override
        public String render(String driverName) {
            return (driver == null) ? "Select a driver" : driverName;
        }
    });

    private RequestContext requestContext;
    
    /**
     * @param eventBus
     * @param resources
     */
    public SqlConnectionEditor(EventBus eventBus, Resources resources) {
        super(eventBus, resources);
        initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

        driver = GWT.create(Driver.class);
        driver.initialize(this);

    }
    
    @Override
    protected ErrorPanel getErrorPanel() {
        // TODO Auto-generated method stub
        return null;
    }
    

    @UiHandler("save")
    void onSave(ClickEvent event) {
       // presenter.onSave();
    }

    @UiHandler("clear")
    void onClear(ClickEvent event) {
       clearForm();
    }
    
    private void clearForm() {
        availableDrivers.setValue(null);
    }

    @Override
    public void reset() {
        availableDrivers.setValue(null);
    }
    
    public void setAvailableDrivers(List<String> availableDrivers) {
        this.availableDrivers.setAcceptableValues(availableDrivers);
    }

    public RequestFactoryEditorDriver<SqlConnectionProxy, ?> getEditorDriver() {
        return driver;
    }

    @Override
    public void setRequestContext(RequestContext requestContext) {
       this.requestContext = requestContext;
        
    }
}
