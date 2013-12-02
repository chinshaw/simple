package com.simple.original.client.view.desktop;

import java.util.Date;
import java.util.logging.Logger;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.ActionCell.Delegate;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.Handler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.api.domain.GroupMembership;
import com.simple.original.client.proxy.AnalyticsTaskExecutionProxy;
import com.simple.original.client.proxy.PersonProxy;
import com.simple.original.client.proxy.QuartzCronTriggerProxy;
import com.simple.original.client.proxy.QuartzJobExecutionContextProxy;
import com.simple.original.client.proxy.QuartzTriggerProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.utils.DateFormats;
import com.simple.original.client.view.IAnalyticsTaskExecutionView.Filter;
import com.simple.original.client.view.IAnalyticsTasksSchedulerView;
import com.simple.original.client.view.widgets.EnumEditor;
import com.simple.original.client.view.widgets.ErrorPanel;
import com.simple.original.client.view.widgets.TextLinkCell;
/**
 * @author chinshaw
 * 
 */
public class AnalyticsTasksSchedulerView extends AbstractView implements IAnalyticsTasksSchedulerView {
	
	/**
     * Logger instance.
     */   
	private static final Logger logger = Logger.getLogger(AnalyticsTasksSchedulerView.class.getName());

	 Column<AnalyticsTaskExecutionProxy, String> ownerColumn = null;

    @UiField
    ErrorPanel errorPanel;

    @UiField(provided = true)
    CellTable<QuartzJobExecutionContextProxy> runningTasks;

    @UiField(provided = true)
    CellTable<QuartzTriggerProxy> allTasks;
    
	/**
	 * Pagination for displaying the list of all tasks.
	 */
	@UiField(provided = true)
	SimplePager pager;

    /**
     * Text box to enter the search criteria.
     */
    @UiField
    TextBox searchHistoryText;  
    
    /**
     * Text box used when searching schedules.
     */
    @UiField
    TextBox searchSchedulesText;
    
    /**
     * List of available Operations list for User view.
     */
	/**
	 * List of tasks.
	 */
	@UiField(provided = true)
	EnumEditor<Filter> historyFilter = new EnumEditor<Filter>(Filter.class);

    @UiField
    HTMLPanel runningTasksPanel;
	/**
	 * FastforwardRows size to the simplepager constructor
	 */
	private static int DEFAULT_FAST_FORWARD_ROWS = 1000;    

    @UiField(provided = true)
    CellTable<AnalyticsTaskExecutionProxy> executedTasks;

    final DateTimeFormat timeFormat = DateFormats.getTableDateFormat();

    @UiTemplate("AnalyticsTasksSchedulerView.ui.xml")
    public interface Binder extends UiBinder<Widget, AnalyticsTasksSchedulerView> {
    }

    private Presenter presenter = null;

    //private GroupMembership userRole;
    
	//private final String appendText = " Tasks";
	@Inject
    public AnalyticsTasksSchedulerView(EventBus eventBus, Resources resources) {
        super(eventBus, resources);
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.LEFT, pagerResources, false, DEFAULT_FAST_FORWARD_ROWS, true);

        initRunningTasksTable();
        initAllTasksTable();
        initExecutedTasks();

        initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
        setupSimplePager();
    }
    
	/**
	 * This method is called in AnalyticsTasksSchedulerViewImpl for displaying of
	 * pagination for all tasks
	 */
	public void setupSimplePager() {
		pager.setDisplay(allTasks);
	}
    
    @Override
    public void setUserView(GroupMembership userGroup) {
    }

    /**
     * This is to show owner Column 
     */   
    public void showOwnerColumn() {
    	 ownerColumn = new Column<AnalyticsTaskExecutionProxy, String>(new TextCell()) {
             @Override
             public String getValue(AnalyticsTaskExecutionProxy analytics) {             	
             	if(analytics.getAnalyticsTask().isPublic()){
             		return "Public";
             	}else{
             	    PersonProxy owner = analytics.getAnalyticsTask().getOwner();
             		if(owner != null){
                 		return owner.getName();               		
                 	}else{
                 		return "";
                 	}
             	}
             }
         };
         ownerColumn.setSortable(true);
         logger.info("show owner column called.....");
         executedTasks.addColumn(ownerColumn, "Owner", "");
    }
    
    /**
     * Function to hide the Owner Column
     */
    public void hideOwnerColumn() {
    	if(ownerColumn != null){
    		executedTasks.removeColumn(ownerColumn);
    		ownerColumn = null;
    	}
    }
    
    private void initRunningTasksTable() {

        runningTasks = new CellTable<QuartzJobExecutionContextProxy>(100, getResources());
        runningTasks.setEmptyTableWidget(new Label("No analytics jobs currently running..."));

        Column<QuartzJobExecutionContextProxy, String> description = new Column<QuartzJobExecutionContextProxy, String>(new TextCell()) {

            @Override
            public String getValue(QuartzJobExecutionContextProxy jobContext) {
                if (jobContext.getTrigger() == null) {
                    return "Error retrieving description";
                }
                return jobContext.getTrigger().getDescription();
            }
        };

        Column<QuartzJobExecutionContextProxy, Date> startTime = new Column<QuartzJobExecutionContextProxy, Date>(new DateCell(timeFormat)) {

            @Override
            public Date getValue(QuartzJobExecutionContextProxy jobContext) {
                return jobContext.getFireTime();
            }
        };

        Column<QuartzJobExecutionContextProxy, String> runningTime = new Column<QuartzJobExecutionContextProxy, String>(new TextCell()) {

            @Override
            public String getValue(QuartzJobExecutionContextProxy jobContext) {
                if (jobContext.getJobRunTime() == -1) {
                    return "Waiting";
                }
                long totalSeconds = (jobContext.getJobRunTime() / 1000);
                long minutes = totalSeconds / 60;
                long seconds = totalSeconds % 60;
                return "" + minutes + " minutes " + seconds + " seconds";
            }
        };

        Column<QuartzJobExecutionContextProxy, Date> lastFired = new Column<QuartzJobExecutionContextProxy, Date>(new DateCell(timeFormat)) {

            @Override
            public Date getValue(QuartzJobExecutionContextProxy jobContext) {
                return jobContext.getPreviousFireTime();
            }
        };

        Column<QuartzJobExecutionContextProxy, Date> scheduledTime = new Column<QuartzJobExecutionContextProxy, Date>(new DateCell(timeFormat)) {

            @Override
            public Date getValue(QuartzJobExecutionContextProxy jobContext) {
                return jobContext.getScheduledFireTime();
            }
        };
        TextLinkCell<QuartzJobExecutionContextProxy> stopCell = new TextLinkCell<QuartzJobExecutionContextProxy>("stop", new Delegate<QuartzJobExecutionContextProxy>() {

            @Override
            public void execute(QuartzJobExecutionContextProxy jobContext) {
                presenter.onStopScheduledTask(jobContext);

            }
        });

        Column<QuartzJobExecutionContextProxy, QuartzJobExecutionContextProxy> stopTask = new Column<QuartzJobExecutionContextProxy, QuartzJobExecutionContextProxy>(stopCell) {

            @Override
            public QuartzJobExecutionContextProxy getValue(QuartzJobExecutionContextProxy jobContext) {
                return jobContext;
            }
        };

        runningTasks.addColumn(description, "Description", "");
        runningTasks.addColumn(lastFired, "Last Fired", "");
        runningTasks.addColumn(scheduledTime, "Schedule Fire Time", "");
        runningTasks.addColumn(startTime, "Start Time", "");
        runningTasks.addColumn(runningTime, "Running Time", "");
        runningTasks.addColumn(stopTask, "Stop", "");
    }

    private void initAllTasksTable() {
        allTasks = new CellTable<QuartzTriggerProxy>(15, getResources());

        Column<QuartzTriggerProxy, String> description = new Column<QuartzTriggerProxy, String>(new TextCell()) {

            @Override
            public String getValue(QuartzTriggerProxy trigger) {
                return trigger.getDescription();
            }
        };
        allTasks.addColumn(description, "Description", "");

        Column<QuartzTriggerProxy, String> triggerKey = new Column<QuartzTriggerProxy, String>(new TextCell()) {

            @Override
            public String getValue(QuartzTriggerProxy trigger) {
                return trigger.getKey().getName() + trigger.getKey().getGroup();
            }
        };
        
        allTasks.addColumn(triggerKey, "Trigger Key", "");

        Column<QuartzTriggerProxy, Date> startTime = new Column<QuartzTriggerProxy, Date>(new DateCell(timeFormat)) {

            @Override
            public Date getValue(QuartzTriggerProxy trigger) {
                return trigger.getStartTime();
            }
        };
        
        allTasks.addColumn(startTime, "Start Time", "");

        DateCell endTimeCell = new DateCell(timeFormat) {
            private final SafeHtmlRenderer<String> myRenderer = SimpleSafeHtmlRenderer.getInstance();

            @Override
            public void render(Context context, Date value, SafeHtmlBuilder sb) {
                if (value != null) {
                    sb.append(myRenderer.render(timeFormat.format(value)));
                } else {
                    sb.append(myRenderer.render("Never Expires"));
                }
            }
        };

        Column<QuartzTriggerProxy, Date> nextFire = new Column<QuartzTriggerProxy, Date>(new DateCell(timeFormat)) {

            @Override
            public Date getValue(QuartzTriggerProxy trigger) {
                return trigger.getNextFireTime();
            }
        };
        allTasks.addColumn(nextFire, "Next Fire", "");

        Column<QuartzTriggerProxy, Date> endTime = new Column<QuartzTriggerProxy, Date>(endTimeCell) {

            @Override
            public Date getValue(QuartzTriggerProxy trigger) {
                if (trigger instanceof QuartzCronTriggerProxy) {
                    ((QuartzCronTriggerProxy) trigger).getExpressionSummary();
                }
                return trigger.getEndTime();
            }
        };
        allTasks.addColumn(endTime, "End Time", "");

        
        ActionCell<QuartzTriggerProxy> runNowCell = new ActionCell<QuartzTriggerProxy>("Fire Now", new Delegate<QuartzTriggerProxy>() {

            @Override
            public void execute(QuartzTriggerProxy trigger) {
                presenter.onRunNow(trigger.getKey());
            }
        });

        Column<QuartzTriggerProxy, QuartzTriggerProxy> runNowColumn = new Column<QuartzTriggerProxy, QuartzTriggerProxy>(runNowCell) {

            @Override
            public QuartzTriggerProxy getValue(QuartzTriggerProxy trigger) {
                return trigger;
            }
        };

        allTasks.addColumn(runNowColumn, "Trigger", "");

        
        ActionCell<QuartzTriggerProxy> detailsCell = new ActionCell<QuartzTriggerProxy>("Details", new Delegate<QuartzTriggerProxy>() {

            @Override
            public void execute(QuartzTriggerProxy trigger) {
                presenter.onDetailsSelected(trigger.getKey());
            }
        });

        Column<QuartzTriggerProxy, QuartzTriggerProxy> detailsColumn = new Column<QuartzTriggerProxy, QuartzTriggerProxy>(detailsCell) {

            @Override
            public QuartzTriggerProxy getValue(QuartzTriggerProxy trigger) {
                return trigger;
            }
        };

        allTasks.addColumn(detailsColumn, "Details", "");
    }   

    private void initExecutedTasks() {
        executedTasks = new CellTable<AnalyticsTaskExecutionProxy>(15, getResources());

        Column<AnalyticsTaskExecutionProxy, String> taskName = new Column<AnalyticsTaskExecutionProxy, String>(new TextCell()) {

            @Override
            public String getValue(AnalyticsTaskExecutionProxy taskExecution) {
                if (taskExecution.getAnalyticsTask() == null) {
                    return "Unknown Task";
                }
                return taskExecution.getAnalyticsTask().getName();
            }
        };
        
        Column<AnalyticsTaskExecutionProxy, Date> startTime = new Column<AnalyticsTaskExecutionProxy, Date>(new DateCell(timeFormat)) {

            @Override
            public Date getValue(AnalyticsTaskExecutionProxy taskExecution) {
                return taskExecution.getStartTime();
            }
        };

        Column<AnalyticsTaskExecutionProxy, Date> completedTime = new Column<AnalyticsTaskExecutionProxy, Date>(new DateCell(timeFormat)) {

            @Override
            public Date getValue(AnalyticsTaskExecutionProxy taskExecution) {
                return taskExecution.getCompletionTime();
            }
        };

        Column<AnalyticsTaskExecutionProxy, String> completionStatus = new Column<AnalyticsTaskExecutionProxy, String>(new TextCell()) {

            @Override
            public String getValue(AnalyticsTaskExecutionProxy taskExecution) {
                if (taskExecution.getCompletionStatus() != null) {
                    return taskExecution.getCompletionStatus().name();
                } else {
                    return "Unknown completion status";
                }
            }
        };
        
        executedTasks.addColumn(taskName, "Analytics Task", "");
        executedTasks.addColumn(startTime, "Started", "");
        executedTasks.addColumn(completedTime, "Completed", "");
        executedTasks.addColumn(completionStatus, "Completion Status", "");
    }

    /**
     * Getter for the error panel
     * 
     * @see com.simple.original.client.view.desktop.AbstractView#getErrorPanel()
     */
    @Override
    protected ErrorPanel getErrorPanel() {
        return errorPanel;
    }

    /**
     * Reset the display, clears the error panel.
     * 
     * @see com.simple.original.client.view.desktop.AbstractView#reset()
     */
    @Override
    public void reset() {
        errorPanel.clear();
    }

    /**
     * Set the presenter.
     */
    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public HasData<QuartzTriggerProxy> getAllTasksDisplay() {
    	return allTasks;
    }

    @Override
    public HasData<QuartzJobExecutionContextProxy> getRunningTasksDisplay() {
        return runningTasks;
    }

	@Override
	public CellTable<AnalyticsTaskExecutionProxy> getDisplayTable() {
		return executedTasks;
	}

	@Override
	public TextBox getHistorySearchText() {
		return searchHistoryText;
	}
	
	public TextBox getSchedulesSearchText() {
		return searchSchedulesText;
	}
	
	@UiHandler("searchSchedules")
	void onSearchSchedules(ClickEvent click) {
		presenter.onSearchSchedules(getSchedulesSearchText().getValue());
	}
	

	@Override
	public int getColumnIndex(Column<AnalyticsTaskExecutionProxy, ?> column) {
		return this.executedTasks.getColumnIndex(column);
	}

	@Override
	public HandlerRegistration addColumnSortHandler(Handler handler) {
		return executedTasks.addColumnSortHandler(handler); 
	}
}
