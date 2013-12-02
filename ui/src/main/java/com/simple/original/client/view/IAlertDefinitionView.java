package com.simple.original.client.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.simple.original.api.domain.GroupMembership;
import com.simple.original.client.proxy.AnalyticsTaskMonitorProxy;
import com.simple.original.client.view.widgets.ErrorPanel;


/**
 * ReportEditorView is the binding interface between the view and the Activity/Presenter.
 * @author chinshaw
 */
public interface IAlertDefinitionView extends IView {
    
    /**
     * This is the p in mvp, can also be an activity.
     */
    public interface Presenter {
        /**
         * Called when delete button is clicked.
         * 
         */
        public void onDeleteAlerts();

        /**
         * Called when add button is clicked.
         */
        public void onNewAlert();

        /**
         * Called when edit button is clicked.
         */
        public void onEditAlert();
        
        /**
         * Called when notify button is clicked.
         */
        public void onNotifyAlert();

		/**
		 * Perform actions when check box clicked
		 * 
		 * @param object
		 * @param value
		 */
		public void onCheckBoxClicked(AnalyticsTaskMonitorProxy object, Boolean value);
		
		
		public void onAdminListSelected(String recordSelected);
        
      }

    /**
     * This method is used to populate values to alerttable.
     */
    public  CellTable<AnalyticsTaskMonitorProxy> getAlertsTable();

    /**
     * This method is used to set the presenter.
     * @param presenter
     */
    public void setPresenter(Presenter presenter);
    
    /**
     * This method is used to delete the selected alerts from database.
     * @return the deleted alerts 
     */
    public Button getSelectedAlertsToDelete();
    
    /**
     * This method is used to set the selected alerts to be deleted.
     * @param deleteSelectedAlerts alerts to be deleted are set
     */
    public void setSelectedAlertsToDelete(Button deleteSelectedAlerts);
    
    /**
     * This method is used to get search textbox.
     * @return
     */
    public TextBox getSearchQueryInput();
    
    /**
     * This method is used to add the columns to be sorted.
     * @param handler
     * @return HandlerRegistration
     */
    public HandlerRegistration addColumnSortHandler(ColumnSortEvent.Handler handler);
    
    /**
     * This method gives the column index on which sort funtionality is to be performed.
     * @param column
     * @return int column index
     */
    public int getColumnIndex(Column<AnalyticsTaskMonitorProxy, ?> column);

	/**
     * get errorPanel
	 * @return
	 */
	public ErrorPanel getErrorPanel();

	/**
	 * To set the user Admin/UserView.
	 * @param groupMembership
	 */
	public void setUserView(GroupMembership userGroup);

	void setEnabledNotifyButton(boolean value);

	void setEnabledEditButton(boolean value);

	void setEnabledDeleteButton(boolean value);

}
