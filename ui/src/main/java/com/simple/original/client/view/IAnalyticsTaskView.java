package com.simple.original.client.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.TextBox;
import com.simple.original.client.proxy.AnalyticsTaskProxy;
import com.simple.original.client.view.widgets.ErrorPanel;

/**
 * AnalyticsTasksView is the binding interface between the view and the Activity/Presenter.
 * @author chinshaw
 */
public interface IAnalyticsTaskView extends IView {

    /**
     * This is the p in mvp, can also be an activity.
     */
    public interface Presenter {
        /**
         * Called when the select all header is clicked.
         * @param allSelected true to select all, false if not
         */
        public void onSelectAll(Boolean allSelected);

        public void onDeleteTasks();

        public void onNewTask();

        public void onEditTask();

        public void onExportAnalyticsTasks();

        public void onImportAnalyticsTasks();

        public void onClickSearch();

		/**
		 * this method is called on change of the list item 
		 */
        public void updateRecordType(String recordType);

		public void onCopyAnalyticsTask();

		public void onCheckBoxClicked(Boolean value);

		public void onSubscription(AnalyticsTaskProxy analyticsTask);

    }

    public void setPresenter(Presenter presenter);

    CellTable<AnalyticsTaskProxy> getAnalyticsTasksTable();

	/**
     * This method is used to get the Analytics tasks matching the criteria given in the search textbox.
     * @return
     */
    public TextBox getSearchText();

    /**
     * This method is used to add the columns to be sorted.
     * @param handler
     * @return HandlerRegistration
     */
    public HandlerRegistration addColumnSortHandler(ColumnSortEvent.Handler handler);

    /**
     * This method is used to get the column index of sortable column.
     * @return
     */
	public int getColumnIndex(Column<AnalyticsTaskProxy, ?> column);
	
	/**
	 * returns error panel
	 * @return
	 */
	public ErrorPanel getErrorPanel();
	
	/**
	 * To make the delete button disable for Report Manager View.
	 * @param value
	 */
	void setEnabledDeleteTask(Boolean value);
	
	/**
	 * To make the Copy button disable.
	 * @param value
	 */
	public void setEnabledCopyTask(boolean value);

	/**
	 * To make the Edit button disable.
	 * @param value
	 */
	void setEnabledEditTask(Boolean value);
}
