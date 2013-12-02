/**
 * 
 */
package com.simple.original.client.view;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.simple.original.client.proxy.SqlConnectionProxy;

/**
 * @author chinshaw
 *
 */
public interface IDataProvidersView extends IView {

    public interface Presenter {
        
        void onSave();

        void onTestConnections(Set<SqlConnectionProxy> selectedSet);
        
    }
    
    void setPresenter(Presenter presenter);

    RequestFactoryEditorDriver<SqlConnectionProxy, ?> getEditorDriver();

    CellTable<SqlConnectionProxy> getConfiguredDataProviders();

    void setAvailableDrivers(List<String> drivers);
}
