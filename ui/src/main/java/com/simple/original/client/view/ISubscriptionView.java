package com.simple.original.client.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.view.client.HasData;
import com.simple.original.client.proxy.SubscriptionProxy;
import com.simple.original.client.view.widgets.ErrorPanel;

public interface ISubscriptionView extends IView {

	public interface Presenter {
		 /**
         * Called when the check box is clicked.
         * @param allSelected true to select all, false if not
         */
		void onCheckBoxClicked(SubscriptionProxy object, Boolean value,int index);

		void onSaveSubscriptions();

		void onCancelSubscriptions();
	}

	public void setPresenter(Presenter presenter);

	HasWidgets getContentPanel();

	/**
	 * This method gives the column index on which sort functionality is to be
	 * performed.
	 * 
	 * @param column
	 * @return int column index
	 */
	public int getColumnIndex(Column<SubscriptionProxy, ?> column);

	HasData<SubscriptionProxy> getSubscriptionsTable();
	
	public HandlerRegistration addColumnSortHandler(ColumnSortEvent.Handler handler);

	public Button getCancelSubscriptions();

	public Button getSaveSubscriptions();

	public ErrorPanel getErrorPanel();
}
