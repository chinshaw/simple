package com.simple.original.client.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.api.domain.RecordFecthType;
import com.simple.original.api.domain.SortOrder;
import com.simple.original.client.proxy.DatastoreObjectProxy;
import com.simple.original.client.service.DaoRequestFactory.DaoRequest;

/**
 * This class provides a base / default implementation of a data provider for
 * all the data views that implement pagination and may have subscribed to
 * asynch comet updates.
 * 
 * @author valva
 * 
 */
public abstract class BaseListDataProvider<T extends DatastoreObjectProxy> extends AsyncDataProvider<T> implements ColumnSortEvent.Handler {

    private static final Logger logger = Logger.getLogger(BaseListDataProvider.class.getName());

    private boolean updateInProgress = false;

    /**
     * ProvidesKey implementation
     */
    public static final ProvidesKey<DatastoreObjectProxy> KeyProvider = new ProvidesKey<DatastoreObjectProxy>() {

        @Override
        public Long getKey(DatastoreObjectProxy item) {
            return item.getId();
        }
    };

    /**
     * Constructor
     */
    public BaseListDataProvider() {
        super(new ProvidesKey<T>() {
            public Long getKey(T item) {
                return (item == null) ? null : item.getId();
            }
        });
    }

    /**
     * This gets called when the rage of data is changed. The method calls the
     * DaoRequestFactory methods to get count of the records and the data for
     * current display. This method uses a boolean to tell if there is a update
     * already in progress because the method setRowCount fires an event that will
     * call onRangeChanged for all values in the queue. If the number of values
     * returned from the query is less than the number of rows in the selected
     * range then the row count will be set exact for the table in the pager.
     */
    @Override
    protected void onRangeChanged(final HasData<T> display) {

        if (updateInProgress) {
            return;
        }

        final Range range = display.getVisibleRange();

        Request<List<T>> request = getRecordsQuery(range);
        updateInProgress = true;
        request.fire(new Receiver<List<T>>() {

            @Override
            public void onSuccess(List<T> values) {
                updateInProgress = false;
                int rowCount = values.size();
                if (rowCount < range.getLength()) {
                    getDisplayTable().setRowCount(range.getStart() + rowCount , true);
                }else{
                	getDisplayTable().setRowCount(range.getStart() , false);
                }
                updateRowData(range.getStart(), values);
            }

            @Override
            public void onFailure(ServerFailure error) {
                updateInProgress = false;
                logger.info("BaseListDataProvider.onRangeChanged().onFailure() -> failed to load data with error: " + error.getMessage());
                showError("Failed to get the data with error -  " + error.getMessage());
                getDisplayTable().setRowCount(0, true);
                getDisplayTable().setEmptyTableWidget(new Label("Failed to retrieve the requested data."));
            }
        });
        logger.fine("BaseListDataProvider.onRangeChanged() finished");
    }

    /**
     * This method is called when the EntityCreateEvent is fired and received by
     * the subscribed activity classes
     * 
     * @param ids
     *            List of entity id's created
     */
    public void appendCometData(Set<Long> ids) {

        if (!getPager().hasNextPage()  
				&& getDisplayTable().getVisibleItemCount() < getDisplayTable().getPageSize()) {
            /*
             * There isn't a next page and the display can have more rows. Add
             * the new record to the table
             */
            getFindListQuery(ids).fire(new Receiver<List<T>>() {

                @Override
                public void onSuccess(List<T> response) {
					updateRowData(getDisplayTable().getPageStart() + getDisplayTable().getVisibleItemCount(), response);
                    logger.info("BaseListDataProvider.appendCometData().onSuccess(): Append to the display is complete ");
                }

                @Override
                public void onFailure(ServerFailure error) {
                    logger.warning("BaseListDataProvider.appendCometData().onFailure(): failed to refresh the comet data : " + error.getMessage());
                }

            });

        }

    }

    /**
     * Fetches the updated data for the list of id's and updates the rows in the
     * GUI
     * 
     * @param ids
     *            List of id's that need to be refreshed
     */
    public void updateCometData(Set<Long> ids) {

        if (updateInProgress) {
            logger.info("BaseListDataProvider.updateCometData(): Update in progress ... returning");
            return;
        }

        /* List of the ids to refresh */
		final Set<Long> refreshList = new HashSet<Long>();

		for (T proxy : getDisplayTable().getVisibleItems()) {

            /*
             * Check if the id is present in the GUI display. This will be used
             * to get the position in the display later
             */
			if(ids.contains(proxy.getId())) {
				/* record the proxies to be refreshed */
				refreshList.add(proxy.getId());
			}
		}

		if (refreshList.size() > 0) {
			getFindListQuery(refreshList).fire(new Receiver<List<T>>()  {
    			
    			@Override
                public void onSuccess(List<T> response) {
    							logger.fine("BaseListDataProvider.updateCometData().onSuccess: Got the refreshed Alerts of size:  " + response.size());
    				
                	for (T proxy : response) {
                		logger.fine("BaseListDataProvider.updateCometData().onSuccess: id : " + proxy.getId());
                		
                		/* Replace with the refreshed data */                		
                		int index = getDisplayTable().getVisibleItems().indexOf(proxy);                		
                		if(!updateInProgress && index != -1) {
	                		getDisplayTable().setRowData(
	                				getDisplayTable().getPageStart() + index, Collections.singletonList(proxy));
	                		logger.info("BaseListDataProvider.updateCometData().onSuccess: Update to the display is complete for : " + proxy.getClass() + "with id: " + proxy.getId());
                		}
                	}
                }
                @Override
                public void onFailure(ServerFailure error) {
                    logger.warning("BaseListDataProvider.updateCometData().onFailure(): failed to refresh the comet data : " + error.getMessage());
                }
            });
        }

    }

    /**
     * Returns the request factory query to find a list of entities based on
     * their id's
     * 
     * @param ids Entity id's
     * @return request factory query
     */
    protected Request<List<T>> getFindListQuery(Set<Long> ids) {
        return getRequestProvider().findList(ids).with(getWithProperties());
    }

    /**
     * Default implementation for the request factory query to fetch the records
     * 
	 * @param range Display range
     * @return request factory query
     */
    protected Request<List<T>> getRecordsQuery(Range range) {
        return getRequestProvider().search(range.getStart(), range.getLength(),
        			getRecordFecthType(), getSearchText(), getSearchColumn(), 
        										getSortColumn(), getSortOrder()).with(getWithProperties());
    }

    /**
     * Default implementation for the record fetch type
     * 
     * @return RecordFecthType
     */
    protected RecordFecthType getRecordFecthType() {
        return RecordFecthType.ALL_RECORDS;
    }

    /**
     * Default implementation to get search text
     * 
     * @return search text
     */
    protected String getSearchText() {
        return null;
    }

    /**
     * Default implementation to get the search column name
     * 
     * @return search column name
     */
    protected String getSearchColumn() {
        return null;
    }

    /**
     * Default implementation to get the search column name
     * 
     * @return search column name
     */
    protected String getSortColumn() {
        return "1";
    }

    /**
     * Default implementation to get the column sort order
     * 
     * @return SortOrder
     */
    protected SortOrder getSortOrder() {
        return SortOrder.ASCENDING;
    }

    /**
     * Default implementation for query properties to use with()
     * 
     * @return array of properties
     */
    protected String[] getWithProperties() {
        String props[] = {};
        return props;
    }

    /**
     * Default implementation to get the pager from display
     * 
     * @return pager
     */
    protected SimplePager getPager() {
        return null;
    }

    /**
     * This method returns the request factory interface
     * 
     * @return request factory interface
     */
    protected abstract DaoRequest<T> getRequestProvider();

    /**
     * This method returns the reference to the display CellTable
     * 
     * @return
     */
    protected abstract CellTable<T> getDisplayTable();

    /**
     * This method displays the message passed as parameter on the display as
     * error message
     * 
     * @param errorMessage
     */
    protected abstract void showError(String errorMessage);
}
