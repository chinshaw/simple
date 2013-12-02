package com.simple.original.client.view;


import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.HasData;
import com.simple.original.client.proxy.AnalyticsTaskExecutionProxy;


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

    HasData<AnalyticsTaskExecutionProxy> getHistoryDisplay();

    @Ignore
    DateBox getScheduleStartDate();
}
