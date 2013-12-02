package com.simple.original.client.view;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;
import com.simple.original.client.dashboard.AbstractDashboardWidget;
import com.simple.original.client.dashboard.model.IDashboardModel;
import com.simple.original.client.view.desktop.AnalyticsInputEditor;

public interface IDashboardView extends IView {

    public interface Presenter {
        void onRerunTask();

        void onDateSliderChange(Date date);

        void onEditDashboard();
    }

    public enum WidgetType {
        Gauge("Gauge"), MetricCollectionTable("Metric Collection Table"), DynamicChart("Dynamic Chart"), StaticChart("Static Plot");

        private final String description;

        WidgetType(String description) {
            this.description = description;
        }

        public String getDesription() {
            return this.description;
        }
    }

    HasWidgets getContentPanel();

    AnalyticsInputEditor getInputsEditor();

    void setDebugOutput(String log);

    void setPresenter(Presenter presenter);

    void setModel(IDashboardModel dashboard);

    void setTaskCompletionTime(Date completionTime);

    void setHistory(List<Date> historicalDates);

    void showTaskOutput(String output);
    
    public List<AbstractDashboardWidget<?>> getDashboardWidgets(); 
}
