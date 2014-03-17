/**
 * 
 */
package com.simple.original.client.view.desktop;

import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.simple.original.client.proxy.SqlConnectionProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IDataProvidersView;
import com.simple.original.client.view.widgets.CheckboxHeader;
import com.simple.original.client.view.widgets.ErrorPanel;

/**
 * @author chinshaw
 * 
 */
public class DataProvidersView extends AbstractView implements IDataProvidersView {


    /**
     * This is the uibinder and it will use the view.DefaultView.ui.xml
     * template.
     */
    @UiTemplate("DataProvidersView.ui.xml")
    public interface Binder extends UiBinder<Widget, DataProvidersView> {
    }


    @UiField(provided = true)
    CellTable<SqlConnectionProxy> configuredProviders;

    @UiField
    SimplePager pager;
    
    private MultiSelectionModel<SqlConnectionProxy> selectionModel;
    
    private RequestContext requestContext;

    private Presenter presenter;


    /**
     * @param eventBus
     * @param resources
     */
    @Inject
    public DataProvidersView(EventBus eventBus, Resources resources) {
        super(eventBus, resources);
        createTable();
        initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
    }

    private void createTable() {
        configuredProviders = new CellTable<SqlConnectionProxy>(15, getResources());
        configuredProviders.setEmptyTableWidget(new Label("No data provides configured"));

        selectionModel = new MultiSelectionModel<SqlConnectionProxy>();
        configuredProviders.setSelectionModel(selectionModel);

        Column<SqlConnectionProxy, Boolean> checkColumn = new Column<SqlConnectionProxy, Boolean>(new CheckboxCell(false, true)) {
            @Override
            public Boolean getValue(SqlConnectionProxy dataProvider) {
                // Get the value from the selection model.
                return selectionModel.isSelected(dataProvider);
            }
        };

        
        Column<SqlConnectionProxy, ImageResource> statusColumn = new Column<SqlConnectionProxy, ImageResource>(new ImageResourceCell()) {
            @Override
            public ImageResource getValue(SqlConnectionProxy dataProvider) {
                return getResources().yellowButtonSmall();
            }
        };
        
        Column<SqlConnectionProxy, String> nameColumn = new Column<SqlConnectionProxy, String>(new TextCell()) {

            @Override
            public String getValue(SqlConnectionProxy driver) {
                return driver.getName();
            }
        };

        Column<SqlConnectionProxy, String> driverColumn = new Column<SqlConnectionProxy, String>(new TextCell()) {

            @Override
            public String getValue(SqlConnectionProxy driver) {
                return driver.getDriverName();
            }
        };

        Column<SqlConnectionProxy, String> hostColumn = new Column<SqlConnectionProxy, String>(new TextCell()) {

            @Override
            public String getValue(SqlConnectionProxy driver) {
                return driver.getHost();
            }
        };

        Column<SqlConnectionProxy, String> userName = new Column<SqlConnectionProxy, String>(new TextCell()) {

            @Override
            public String getValue(SqlConnectionProxy driver) {
                return driver.getUserName();
            }
        };

        CheckboxHeader checkboxHeader = new CheckboxHeader();
        checkboxHeader.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                for (SqlConnectionProxy driver : configuredProviders.getVisibleItems()) {
                    selectionModel.setSelected(driver, event.getValue());
                }
            }
        });
        
        // configuredProviders.addC
        configuredProviders.addColumn(checkColumn, checkboxHeader);
        configuredProviders.setColumnWidth(checkColumn, 2, Unit.EM);
        
        configuredProviders.addColumn(statusColumn, "Status", "");
        configuredProviders.setColumnWidth(statusColumn, 3, Unit.EM);

        configuredProviders.addColumn(nameColumn, "Name", "");
        configuredProviders.addColumn(driverColumn, "Driver", "");
        configuredProviders.addColumn(hostColumn, "Host", "");
        configuredProviders.addColumn(userName, "User Name", "");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.simple.original.client.view.DataProvidersView#setPresenter(com.simple.original.client
     * .view.DataProvidersView.Presenter)
     */
    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.simple.original.client.view.desktop.ViewImpl#getErrorPanel()
     */
    @Override
    protected ErrorPanel getErrorPanel() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.simple.original.client.view.desktop.ViewImpl#reset()
     */
    @Override
    public void reset() {
    }

    @Override
    public RequestFactoryEditorDriver<SqlConnectionProxy, ?> getEditorDriver() {
        return null;
    }

    @Override
    public CellTable<SqlConnectionProxy> getConfiguredDataProviders() {
        return configuredProviders;
    }

    @Override
    public void setAvailableDrivers(List<String> drivers) {
        // TODO Auto-generated method stub
        
    }
}
