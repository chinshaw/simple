package com.simple.original.client.activity;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.client.events.NotificationEvent;
import com.simple.original.client.place.AnalyticsTaskSchedulerDetailsPlace;
import com.simple.original.client.place.AnalyticsTasksSchedulerPlace;
import com.simple.original.client.place.HistoricalMetricsPlace;
import com.simple.original.client.proxy.AnalyticsTaskExecutionProxy;
import com.simple.original.client.proxy.QuartzJobExecutionContextProxy;
import com.simple.original.client.proxy.QuartzTriggerKeyProxy;
import com.simple.original.client.proxy.QuartzTriggerProxy;
import com.simple.original.client.service.DaoBaseDataProvider;
import com.simple.original.client.service.DaoRequestFactory.DaoRequest;
import com.simple.original.client.service.ServiceRequestFactory.SchedulerRequest;
import com.simple.original.client.view.IAnalyticsTasksSchedulerView;
import com.simple.original.client.view.IAnalyticsTasksSchedulerView.Presenter;

public class AnalyticsTasksSchedulerActivity extends AbstractActivity<AnalyticsTasksSchedulerPlace, IAnalyticsTasksSchedulerView> implements Presenter {

	/**
     * Logger instance.
     */   
	private static final Logger logger = Logger.getLogger(AnalyticsTasksSchedulerActivity.class.getName());	
	
    private class ExecutedTasksDataProvider extends DaoBaseDataProvider<AnalyticsTaskExecutionProxy> implements ColumnSortEvent.Handler {

 
		@Override
        public String[] getWithProperties() {
			return AnalyticsTaskExecutionProxy.HISTORY_PROPERTIES;
        }

		@Override
		public void onColumnSort(ColumnSortEvent event) {
			// TODO Auto-generated method stub
		}

		@Override
		public DaoRequest<AnalyticsTaskExecutionProxy> getRequestProvider() {
			return dao().analyticsTaskExecutionRequest();
		}
	}
     
    
    private ListDataProvider<QuartzTriggerProxy> allSchedulesDataProvider = new ListDataProvider<QuartzTriggerProxy>();
    
    private ExecutedTasksDataProvider historyDataProvider = new ExecutedTasksDataProvider();
    
    private SingleSelectionModel<AnalyticsTaskExecutionProxy> historySelectionModel = new SingleSelectionModel<AnalyticsTaskExecutionProxy>(historyDataProvider.getKeyProvider());
    
    @Inject
    public AnalyticsTasksSchedulerActivity(IAnalyticsTasksSchedulerView view) {
    	super(view);
    }

    @Override
    protected void bindToView() {
    	display.setPresenter(this);
        
        allSchedulesDataProvider.addDataDisplay(display.getAllTasksDisplay());
        
        SchedulerRequest request = service().schedulerRequest();
        
        request.getScheduledJobs().with(QuartzTriggerProxy.PROPERTIES).to(new Receiver<List<QuartzTriggerProxy>>() {

            @Override
            public void onSuccess(List<QuartzTriggerProxy> triggers) {
                allSchedulesDataProvider.setList(triggers);
                
            }
            @Override
            public void onFailure(ServerFailure failure) {
               display.showError("Error occurred while communicating with task eninge ", failure);
            }
        });
        
        request.getAllRunningJobs().with(QuartzJobExecutionContextProxy.PROPERTIES).to(new Receiver<List<QuartzJobExecutionContextProxy>>() {

            @Override
            public void onSuccess(List<QuartzJobExecutionContextProxy> response) {
                ListDataProvider<QuartzJobExecutionContextProxy> runningTasksDataProvider = new ListDataProvider<QuartzJobExecutionContextProxy>();
                runningTasksDataProvider.setList(response);
                runningTasksDataProvider.addDataDisplay(display.getRunningTasksDisplay());
            }
        });
        
        
        request.fire();
        
        // Get the historical executed jobs.
        historyDataProvider.addDataDisplay(display.getDisplayTable());
        
        // Hook up the selection handler.
        historySelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
            	placeController().goTo(new HistoricalMetricsPlace(historySelectionModel.getSelectedObject().getId()));
            }
        });
        display.getDisplayTable().setSelectionModel(historySelectionModel);

    }


	@Override
	public void onDetailsSelected(QuartzTriggerKeyProxy triggerKey) {
		placeController().goTo(new AnalyticsTaskSchedulerDetailsPlace(triggerKey));
	}

    @Override
    public void onStopScheduledTask(QuartzJobExecutionContextProxy jobContext) {
        if (jobContext == null) {
            display.showError("Internal Error: Context was null when deleting job");
        }
        
        if (jobContext.getTrigger() == null) {
            display.showError("Internal Error: Context did not contain a valid trigger");
        }
        
        if (jobContext.getTrigger().getJobKey() == null) {
            display.showError("Internal Error: Context did not contain a valid job key");
        }
        
        service().schedulerRequest().cancelRunningTask(jobContext.getTrigger().getJobKey()).fire(new Receiver<Boolean>() {

            @Override
            public void onSuccess(Boolean response) {
                eventBus().fireEvent(new NotificationEvent("Task stopped successfully"));
            }
        });
    }

	@Override
	public void onOperationSelected(String recordSelected) {
		logger.info("selected record Fetch type ->"+recordSelected);
    	display.getDisplayTable().setVisibleRangeAndClearData(display.getDisplayTable().getVisibleRange(), true);	
	}

	@Override
	public void onRunNow(QuartzTriggerKeyProxy key) {
		service().schedulerRequest().fireTriggerNow(key).fire();
	}

	@Override
	public void onSearchSchedules(String searchString) {
		service().schedulerRequest().searchSchedules(searchString).with(QuartzTriggerProxy.PROPERTIES).fire(new Receiver<List<QuartzTriggerProxy>>() {

			@Override
			public void onSuccess(List<QuartzTriggerProxy> response) {
				allSchedulesDataProvider.setList(response);
			}
		});
	}
}