package com.simple.original.client.view.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.inject.Inject;
import com.simple.original.api.domain.GroupMembership;
import com.simple.original.client.proxy.AnalyticsOperationInputProxy;
import com.simple.original.client.proxy.AnalyticsTaskNameProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.service.ServiceRequestFactory.DashboardRequest;
import com.simple.original.client.view.IAnalyticsTaskExecutionView;
import com.simple.original.client.view.widgets.EnumEditor;
import com.simple.original.client.view.widgets.ErrorPanel;

public class AnalyticsTaskExecView extends AbstractView implements IAnalyticsTaskExecutionView {

    @UiField
    ErrorPanel errorPanel;

    /**
     * List of tasks.
     */
    @UiField(provided = true)
    EnumEditor<Filter> filter = new EnumEditor<Filter>(Filter.class);

    @UiField(provided = true)
    ValueListBox<AnalyticsTaskNameProxy> tasksList = new ValueListBox<AnalyticsTaskNameProxy>(new AbstractRenderer<AnalyticsTaskNameProxy>() {

        @Override
        public String render(AnalyticsTaskNameProxy task) {
            return task == null ? "Select a task" : task.getName();
        }

    }, new ProvidesKey<AnalyticsTaskNameProxy>() {

        @Override
        public Object getKey(AnalyticsTaskNameProxy item) {
            return (item == null) ? null : item.getId();
        }
    });

    @UiField
    TextArea taskDescription;

    @UiField
    AnalyticsInputEditor taskInputsEditor;

    @UiField
    Button executeScript;

    @UiField
    Button saveTask;

    @UiField
    Button scheduleTask;

    private Presenter presenter;

    /**
     * This is the uibinder and it will use the view.DefaultView.ui.xml
     * template.
     */
    @UiTemplate("AnalyticsTaskExecView.ui.xml")
    public interface Binder extends UiBinder<Widget, AnalyticsTaskExecView> {
    }

    @Inject
    public AnalyticsTaskExecView(Resources resources) {
        super(resources);
        initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

        tasksList.addValueChangeHandler(new ValueChangeHandler<AnalyticsTaskNameProxy>() {

            @Override
            public void onValueChange(ValueChangeEvent<AnalyticsTaskNameProxy> event) {
                presenter.onAnalyticsTaskSelected(event.getValue());
            }
        });

        filter.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                presenter.updateTasks();
            }

        });
    }

    @Override
    public void setUserView(GroupMembership groupMembership) {

        /*
         * if (operationSelected.equals(Filter.ALL.name())) { if
         * (groupMembership == GroupMembership.USER) {
         * scheduleTask.setEnabled(true); } setSelectedAnalyticsTask(null); }
         * else if
         * (operationSelected.equals(RecordFecthType.PUBLIC_RECORDS.name())) {
         * if (groupMembership == GroupMembership.USER) {
         * scheduleTask.setEnabled(false); } setSelectedAnalyticsTask(null); }
         */
    }

    @Override
    protected ErrorPanel getErrorPanel() {
        return errorPanel;
    }

    @Override
    public HasClickHandlers getExecuteScriptButton() {
        return executeScript;
    }

    @Override
    public void reset() {
        errorPanel.clear();
        setSelectedAnalyticsTask(null);
        taskDescription.setText("");
        filter.setSelectedIndex(0);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @UiFactory
    AnalyticsInputEditor createInputs() {
        return new AnalyticsInputEditor(getResources());
    }

    @UiHandler("saveTask")
    void onSaveAnalyticsTask(ClickEvent clickEvent) {
        presenter.onSaveTask();
    }

    @UiHandler("scheduleTask")
    void onScheduleAnalyticsTask(ClickEvent clickEvent) {
        presenter.onScheduleTask();
    }

    @Override
    public void setTaskDescription(String description) {
        taskDescription.setValue(description);
    }

    @Override
    public void setSelectedAnalyticsTask(AnalyticsTaskNameProxy selectedTask) {
        if (selectedTask == null) {
            setTaskDescription("");
        }
        tasksList.setValue(selectedTask, true);
    }

    @Override
    public void setAcceptableAnalyticsTasks(List<AnalyticsTaskNameProxy> taskNames) {
        tasksList.setAcceptableValues(taskNames);
    }

    @Override
    public Long getSelectedTaskId() {
        return tasksList.getValue().getId();
    }

    @Override
    public SimpleBeanEditorDriver<List<AnalyticsOperationInputProxy>, ?> getInputsEditorDriver() {
        return taskInputsEditor.getInputsEditorDriver();
    }

    @Override
    public void setRequestContext(DashboardRequest request) {
        taskInputsEditor.setRequestContext(request);
    }

    @Override
    public Filter getFilter() {
        String selectedFilter = filter.getValue(filter.getSelectedIndex());
        return Filter.valueOf(selectedFilter);
    }
}