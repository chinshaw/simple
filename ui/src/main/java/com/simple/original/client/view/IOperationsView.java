package com.simple.original.client.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.Handler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.HasData;
import com.simple.original.api.domain.GroupMembership;
import com.simple.original.client.proxy.AnalyticsOperationProxy;
import com.simple.original.client.view.widgets.ErrorPanel;

public interface IOperationsView extends IView {

    public interface Presenter {
    	/**
    	 * Called on the click of New Button to add the analytics operation.
    	 */
        void onAddAnalytics();

        /**
         * Called on the click of Edit button.
         */
        void onEditAnalytics();
		
        /**
         * Called on the click of copy button to delete the analytics operation.
         */
        void onCopyAnalytics();

        /**
         * Called on the click of Delete button to delete the analytics operation.
         */
		void onDeleteAnalyticsOperations();
		
		/**
		 * Called on the operation selection from the list box.
		 * @param operationSelected 
		 */
		void onOperationSelected(AnalyticsOperationProxy operation);
		
		/**
		 * Called when the select all header is clicked.
		 * @param allSelected true to select all, false if not
		 */
		void onSelectAll(Boolean isChecked);

		/**
		 * Called on the Click of Search Button.
		 */
		void onClickSearch();
		
		void onCheckBoxSelected(AnalyticsOperationProxy object, Boolean value);
    }
    
    /**
     * To set the Presenter.
     * @param analyticsActivity
     */
    void setPresenter(Presenter analyticsActivity);

    /**
	 * returns error panel
	 * @return
	 */
	ErrorPanel getErrorPanel();
    
    /**
     * To get the search Text.
     * @return
     */
    TextBox getSearchText();

	
	/**
     * This method is used to populate the values to analytics operation.
     */
	HasData<AnalyticsOperationProxy> getOperationsList();
	
	
	/**
	 * To Set the User View.
	 * @param userGroup
	 */
	void setUserView(GroupMembership userGroup);

	void setEnabledEditButton(boolean value);

	void setEnabledAddButton(boolean value);

	void setEnabledCopyButton(boolean value);
	
	void disableCopyEditDeleteButtons();

	void setEnabledDeleteButton(boolean value);

}
