package com.simple.original.client.view;


import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.HasData;
import com.simple.api.orchestrator.ITaskExecution;


public interface IAnalyticsTaskSchedulerView extends IView {

    public interface Presenter {
        void onScheduleAnalyticsTask(String cronExpression);
        void onCancel();
        void onUnscheduleAnalyticsTask();
        void onTogglePause();
    }
    
    void setPresenter(Presenter presenter);
    
    void setAnalyticsTaskName(String analyticsTaskName);

    
    String getScheduleName();

    void setTriggerState(String response);

    HasData<ITaskExecution> getHistoryDisplay();

    @Ignore
    DateBox getScheduleStartDate();
}
