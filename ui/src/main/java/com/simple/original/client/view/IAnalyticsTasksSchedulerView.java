package com.simple.original.client.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.Handler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.HasData;
import com.simple.api.domain.GroupMembership;
import com.simple.api.orchestrator.ITaskExecution;
import com.simple.original.client.proxy.QuartzJobExecutionContextProxy;
import com.simple.original.client.proxy.QuartzTriggerKeyProxy;
import com.simple.original.client.proxy.QuartzTriggerProxy;

public interface IAnalyticsTasksSchedulerView extends IView {

    public interface Presenter {

		void onDetailsSelected(QuartzTriggerKeyProxy key);

        void onStopScheduledTask(QuartzJobExecutionContextProxy jobContext);

		void onOperationSelected(String operationSelected);

		void onRunNow(QuartzTriggerKeyProxy key);

		void onSearchSchedules(String value);
    }
    

    HasData<QuartzJobExecutionContextProxy> getRunningTasksDisplay();
    
    HasData<QuartzTriggerProxy> getAllTasksDisplay();
    
    void setPresenter(Presenter presenter);

	void setUserView(GroupMembership userGroup);

	CellTable<ITaskExecution> getDisplayTable();

    /**
     * To get the search Text.
     * @return
     */
    TextBox getHistorySearchText();
    
	/**
	 * Method for getting the column index.
	 * @param column
	 * @return
	 */
	int getColumnIndex(Column<ITaskExecution, ?> column);
	
	/**
	 * Method for implementing sort functionality.
	 * @param handler
	 * @return
	 */
	HandlerRegistration addColumnSortHandler(Handler handler);
}