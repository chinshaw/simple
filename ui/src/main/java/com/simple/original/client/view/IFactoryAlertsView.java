package com.simple.original.client.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.simple.original.client.proxy.MonitorAlertProxy;
import com.simple.original.client.view.widgets.ErrorPanel;

public interface IFactoryAlertsView extends IView {

    public interface Presenter {
    	
		/**
		 * When the user clicks on any Alert Name, It will open a new  page with FactoryAlert details
		 * @param factoryAlertId
		 */
		void onAlertDetailClicked(Long id);

		/**
		 * When the user clicks on any Quix Id, this method is called. It will open a new Quix page with that Quix Id
		 * @param alert
		 */
		void onQuixIdClicked(MonitorAlertProxy alert);

		void onDashboardClicked(Long id);
    }

    void setPresenter(Presenter presenter);

    /**
     * returns factoryAlert table
     * 
     * @return
     */
    CellTable<MonitorAlertProxy> getFactoryAlertTable();
    
    /**
     * textbox to get searchText
     * @return
     */
    public TextBox getSearchText();

	/**
	 * returns index of the column when clicked
	 * @param column
	 * @return
	 */
	int getColumnIndex(Column<MonitorAlertProxy, ?> column);

	/**
	 * list box for search criteria
	 * 
	 * @return
	 */
	public ListBox searchListBox();
	
	/**
	 * search button
	 * @return
	 */
	public Button getSearch();
	
	/**
     * get errorPanel
	 * @return
	 */
	public ErrorPanel getErrorPanel();

	/**
	 *  this method will register if sort is required 
	 * @param handler
	 * @return
	 */
	public HandlerRegistration addColumnSortHandler(ColumnSortEvent.Handler handler);
	
	public SimplePager getPager();

}
