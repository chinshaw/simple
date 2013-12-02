package com.simple.original.client.service;

import java.util.List;
import java.util.Set;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionModel;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.simple.original.client.proxy.DatastoreObjectProxy;
import com.simple.original.client.service.DaoRequestFactory.DaoRequest;

public abstract class DaoBaseDataProvider<T extends DatastoreObjectProxy> extends AsyncDataProvider<T> implements SearchProvider<T> {
    
    public static final ProvidesKey<DatastoreObjectProxy> KeyProvider = new ProvidesKey<DatastoreObjectProxy>() {

        @Override
        public Long getKey(DatastoreObjectProxy item) {
            return item.getId();
        }
    };
    
    public interface HasBaseDataProvider<T extends DatastoreObjectProxy> {
        public void setDataProvider(DaoBaseDataProvider<T> dataProvider);

        public void setSelectionModel(SelectionModel<? super T> selectionModel);
    }

    public DaoBaseDataProvider() {
        super(new ProvidesKey<T>() {
            public Long getKey(T item) {
                // Always do a null check.
                return (item == null) ? null : item.getId();
            }
        });
    }

    @Override
    public void search(String searchText) {

        getSearchQuery(searchText).fire(new Receiver<List<T>>() {

            @Override
            public void onSuccess(List<T> values) {
                updateRowData(0, values);
                updateRowCount(values.size(), true);
            }
        });
    }

    public void showAll() {
        updateRowCount(10, false);
    }

    @Override
    protected void onRangeChanged(HasData<T> display) {
        final Range range = display.getVisibleRange();

        Request<List<T>> request = getRangeQuery(range).with(getWithProperties());

        request.fire(new Receiver<List<T>>() {

            @Override
            public void onSuccess(List<T> values) {
                updateRowData(range.getStart(), values);
            }
        });
    }
    
    public abstract String[] getWithProperties();
    
    public abstract DaoRequest<T> getRequestProvider();
    
    public void remove(final Set<Long> idsToDelete) {
        getDeleteQuery(idsToDelete).fire(new Receiver<Integer>() {

            @Override
            public void onSuccess(Integer response) {
                for (HasData<T> display : getDataDisplays()) {
                    // Force the display to relaod it's view.
                    display.setVisibleRangeAndClearData(display.getVisibleRange(), true);
                }
            }
            
        });
    }

    /**
     * This can be overridden so that you can change the query.
     * 
     * @param range
     * @return
     */
    protected Request<List<T>> getRangeQuery(Range range) {
        return getRequestProvider().find(range.getStart(), range.getLength()).with(getWithProperties());
    }

    protected Request<List<T>> getSearchQuery(String searchText) {
        return getRequestProvider().search(searchText).with(getWithProperties());
    }
    
    protected Request<Integer> getDeleteQuery(Set<Long> idsToDelete) {
        return getRequestProvider().deleteList(idsToDelete);
    }
}
