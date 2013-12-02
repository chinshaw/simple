/**
 * 
 */
package com.simple.original.client.view.desktop;

import java.util.Date;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.simple.original.api.analytics.IAnalyticsTaskExecution.TaskCompletionStatus;
import com.simple.original.client.proxy.AnalyticsTaskExecutionProxy;
import com.simple.original.client.proxy.QuartzTriggerProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.utils.DateFormats;
import com.simple.original.client.view.IAnalyticsTaskSchedulerDetailsView;
import com.simple.original.client.view.widgets.ErrorPanel;
import com.simple.original.client.view.widgets.SpinBox;

/**
 * @author chinshaw
 * 
 */
public class AnalyticsTaskSchedulerDetailsView extends AbstractView implements IAnalyticsTaskSchedulerDetailsView {

    interface EditorDriver extends RequestFactoryEditorDriver<QuartzTriggerProxy, AnalyticsTaskSchedulerDetailsView> {
    }

    @UiTemplate("AnalyticsTaskSchedulerDetailsView.ui.xml")
    interface Binder extends UiBinder<Widget, AnalyticsTaskSchedulerDetailsView> {
    }

    @UiField
    TextBox description;

    @UiField
    SpanElement taskState;

    @UiField
    Button pause;

    @UiField(provided = true)
    CellTable<AnalyticsTaskExecutionProxy> historyTable;

    @UiField
    SimplePager historyPager;

	@Ignore
	@UiField
	DateBox schedulerDateTime;

	@Ignore
	@UiField(provided=true)
	SpinBox Hours;

	@Ignore
	@UiField(provided=true)
	SpinBox Minutes;

	@Ignore
	@UiField
	Image calendarImage;

    private Presenter presenter;

    private final EditorDriver editorDriver = GWT.create(EditorDriver.class);

    @Inject
    public AnalyticsTaskSchedulerDetailsView(EventBus eventBus, Resources resources) {
        super(eventBus, resources);

        Hours = new SpinBox(resources);
        Minutes =new SpinBox(resources);

        initTable();
        initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
        editorDriver.initialize(this);
    }
    
    private void initTable() {
        historyTable = new CellTable<AnalyticsTaskExecutionProxy>(10, getResources());
        
        Column<AnalyticsTaskExecutionProxy, Date> startTime = new Column<AnalyticsTaskExecutionProxy, Date>( new DateCell(DateFormats.getTableDateFormat())) {

            @Override
            public Date getValue(AnalyticsTaskExecutionProxy object) {
                return object.getStartTime();
            }
        };

        Column<AnalyticsTaskExecutionProxy, Date> endTime = new Column<AnalyticsTaskExecutionProxy, Date>( new DateCell(DateFormats.getTableDateFormat())) {

            @Override
            public Date getValue(AnalyticsTaskExecutionProxy execution) {
                return execution.getCompletionTime();
            }
        };
        
        Column<AnalyticsTaskExecutionProxy, String> status = new Column<AnalyticsTaskExecutionProxy, String>( new TextCell()) {

            @Override
            public String getValue(AnalyticsTaskExecutionProxy execution) {
                TaskCompletionStatus completionStatus = execution.getCompletionStatus();
                if (completionStatus != null) {
                    return completionStatus.name();
                } else {
                    return "Unknown";
                }
            }
        };
        
        historyTable.addColumn(startTime, "Start Time", "");
        historyTable.addColumn(endTime, "End Time", "");
        historyTable.addColumn(status, "Completion Status", "");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.simple.original.client.view.desktop.ViewImpl#getErrorPanel()
     */
    @Override
    protected ErrorPanel getErrorPanel() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.simple.original.client.view.desktop.ViewImpl#reset()
     */
    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setAnalyticsTaskName(String analyticsTaskName) {

    }

    @Override
    public String getScheduleName() {
        // TODO Auto-generated method stub
        return null;
    }

    @UiHandler("delete")
    void onUnschedule(ClickEvent event) {
        if (Window.confirm("Are you sure? This will affect the alerts that depend on this task")) {
            presenter.onUnscheduleAnalyticsTask();
        }
    }

    @UiHandler("pause")
    void onPause(ClickEvent event) {
        presenter.onTogglePause();
    }

    @Override
    public RequestFactoryEditorDriver<QuartzTriggerProxy, ?> getEditorDriver() {
        return editorDriver;
    }

    @Override
    public void setTriggerState(String triggerState) {
        taskState.setInnerText(triggerState);
        if (triggerState.equals("PAUSED")) {
            pause.setText("unpause");
        } else {
            pause.setText("pause");
        }
    }

    @Override
    public HasData<AnalyticsTaskExecutionProxy> getHistoryDisplay() {
        return historyTable;
    }

	@Override
	@Ignore
	public DateBox getScheduleStartDate() {
		// TODO Auto-generated method stub
		return schedulerDateTime;
	}
}