package com.simple.original.client.view;

import java.util.List;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.simple.original.api.domain.GroupMembership;
import com.simple.original.client.proxy.AnalyticsOperationInputProxy;
import com.simple.original.client.proxy.AnalyticsTaskNameProxy;
import com.simple.original.client.service.ServiceRequestFactory.AnalyticsRequest;

public interface IAnalyticsTaskExecutionView extends IView {

	public enum Filter {
		All,
		Personal
	}

    public interface Presenter {
        void onAnalyticsTaskSelected(AnalyticsTaskNameProxy selectedTask);

        void onSaveTask();
        
        void onScheduleTask();

		void updateTasks();
    }

    HasClickHandlers getExecuteScriptButton();

    void setTaskDescription(String description);

    void setPresenter(Presenter presenter);

	void setSelectedAnalyticsTask(AnalyticsTaskNameProxy task);

	void setAcceptableAnalyticsTasks(List<AnalyticsTaskNameProxy> tasks);

	Long getSelectedTaskId();
	
	Filter getFilter();
	
	/**
	 * To set the user Admin/User and which task category is being called.
	 * @param groupMembership
	 */
	void setUserView(GroupMembership groupMembership);

	SimpleBeanEditorDriver<List<AnalyticsOperationInputProxy>, ?> getInputsEditorDriver();

	void setRequestContext(AnalyticsRequest factory);
}
