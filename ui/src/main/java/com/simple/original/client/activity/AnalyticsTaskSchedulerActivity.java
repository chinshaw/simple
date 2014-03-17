package com.simple.original.client.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.api.orchestrator.ITaskExecution;
import com.simple.original.client.place.AnalyticsTaskExecPlace;
import com.simple.original.client.place.AnalyticsTaskSchedulerDetailsPlace;
import com.simple.original.client.place.AnalyticsTaskSchedulerPlace;
import com.simple.original.client.place.AnalyticsTasksSchedulerPlace;
import com.simple.original.client.place.HistoricalMetricsPlace;
import com.simple.original.client.proxy.AnalyticsOperationInputProxy;
import com.simple.original.client.proxy.AnalyticsTaskProxy;
import com.simple.original.client.proxy.QuartzJobKeyProxy;
import com.simple.original.client.proxy.QuartzTriggerKeyProxy;
import com.simple.original.client.proxy.QuartzTriggerProxy;
import com.simple.original.client.service.DaoRequestFactory.AnalyticsTaskRequest;
import com.simple.original.client.service.ServiceRequestFactory.SchedulerRequest;
import com.simple.original.client.utils.ClientUtils;
import com.simple.original.client.view.IAnalyticsTaskSchedulerDetailsView;
import com.simple.original.client.view.IAnalyticsTaskSchedulerView;
import com.simple.original.client.view.IAnalyticsTaskSchedulerView.Presenter;

public class AnalyticsTaskSchedulerActivity extends AbstractActivity<AnalyticsTaskSchedulerPlace, IAnalyticsTaskSchedulerView> implements
		Presenter {

	private AnalyticsTaskProxy analyticsTaskBeingScheduled = null;

	private QuartzTriggerKeyProxy triggerKey = null;

	private String triggerState = "NORMAL";

	private static final Logger logger = Logger.getLogger(AnalyticsTaskSchedulerActivity.class.getName());

	private SingleSelectionModel<ITaskExecution> historySelection = new SingleSelectionModel<ITaskExecution>(
			new ProvidesKey<ITaskExecution>() {

				@Override
				public Long getKey(ITaskExecution item) {
					return item.getId();
				}
			});

	@Inject
	public AnalyticsTaskSchedulerActivity(IAnalyticsTaskSchedulerView view) {
		super(view);

	}

	@Override
	protected void bindToView() {
		display.setPresenter(this);

		if (place().getAnalyticsTaskId() == null) {
			display.showError("Analytics Task id cannot be null");
			return;
		}

		AnalyticsTaskRequest findAnalyticsTaskRequest = dao().createAnalyticsTaskRequest();
		findAnalyticsTaskRequest.find(place().getAnalyticsTaskId()).fire(new Receiver<AnalyticsTaskProxy>() {

			@Override
			public void onSuccess(AnalyticsTaskProxy analyticsTask) {
				analyticsTaskBeingScheduled = analyticsTask;
				updateView(analyticsTask);
			}
		});
	}

	private void editSchedule() {
		historySelection.addSelectionChangeHandler(new Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				ITaskExecution proxy = historySelection.getSelectedObject();
				if (proxy != null) {
					placeController().goTo(new HistoricalMetricsPlace(proxy.getId()));
				}
			}
		});
		display.getHistoryDisplay().setSelectionModel(historySelection);

		AnalyticsTaskSchedulerDetailsPlace detailsPlace = (AnalyticsTaskSchedulerDetailsPlace) place();
		SchedulerRequest scheduleRequest = service().schedulerRequest();

		scheduleRequest.getTrigger(detailsPlace.getTriggerName(), detailsPlace.getTriggerGroup()).with(QuartzTriggerProxy.PROPERTIES)
				.to(new Receiver<QuartzTriggerProxy>() {

					@Override
					public void onSuccess(QuartzTriggerProxy trigger) {
						triggerKey = trigger.getKey();

						updateTriggerState(trigger.getKey());
						// Update the history;
						updateAnalyticsTaskData(trigger.getJobKey());

						SchedulerRequest saveRequest = service().schedulerRequest();
						QuartzTriggerProxy editable = saveRequest.edit(trigger);
						saveRequest.rescheduleJob(triggerKey, editable);

						((IAnalyticsTaskSchedulerDetailsView) display).getEditorDriver().edit(trigger, saveRequest);
					}
				});

		scheduleRequest.fire();
	}

	private void updateTriggerState(QuartzTriggerKeyProxy triggerKey) {
		SchedulerRequest scheduleRequest = service().schedulerRequest();

		scheduleRequest.getTriggerState(triggerKey).to(new Receiver<String>() {

			@Override
			public void onSuccess(String response) {
				triggerState = response;
				display.setTriggerState(triggerState);
			}
		});
		scheduleRequest.fire();
	}

	private void updateAnalyticsTaskData(QuartzJobKeyProxy jobKey) {
		SchedulerRequest scheduleRequest = service().schedulerRequest();
		scheduleRequest.getTaskForTrigger(jobKey).fire(new Receiver<AnalyticsTaskProxy>() {

			@Override
			public void onSuccess(AnalyticsTaskProxy response) {
				fetchHistory(response.getId());
			}
		});
	}

	private void fetchHistory(Long analyticsTaskId) {
	}

	private void updateView(AnalyticsTaskProxy analyticsTask) {
		display.setAnalyticsTaskName(analyticsTask.getName());
	}

	@Override
	public void onScheduleAnalyticsTask(String cronExpression) {
		SchedulerRequest scheduleRequest = service().schedulerRequest();

		String scheduleDescription = display.getScheduleName();
		if (scheduleDescription == null || scheduleDescription.length() <= 10) {
			display.showError("Description should be as descriptive as possible to avoid duplication, please give use 10 characters or more.");
		}

		logger.info("Scheduling task " + analyticsTaskBeingScheduled.getId() + " with cron exprssion " + cronExpression
				+ " description is " + display.getScheduleName());

		Date startDate = display.getScheduleStartDate().getValue();

		if (startDate != null) {
			DateTimeFormat.getFormat("dd").format(startDate);
		}

		List<AnalyticsOperationInputProxy> clonedInputs = new ArrayList<AnalyticsOperationInputProxy>();
		ClientUtils.cloneOperationInputs(scheduleRequest, clonedInputs, place().getInputs());

		scheduleRequest.scheduleAnalyticsTask(analyticsTaskBeingScheduled.getId(), clonedInputs, cronExpression, display.getScheduleName(),
				startDate).fire(new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				placeController().goTo(new AnalyticsTasksSchedulerPlace());
			}

			@Override
			public void onFailure(ServerFailure failure) {
				display.showError(failure.getMessage());
			}
		});
	}

	@Override
	public void onUnscheduleAnalyticsTask() {
		AnalyticsTaskSchedulerDetailsPlace pl = (AnalyticsTaskSchedulerDetailsPlace) place();
		service().schedulerRequest().unscheduleJob(pl.getTriggerName(), pl.getTriggerGroup()).fire(new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				placeController().goTo(new AnalyticsTasksSchedulerPlace());
			}
		});
	}

	@Override
	public void onTogglePause() {
		SchedulerRequest scheduleRequest = service().schedulerRequest();

		Receiver<Void> receiver = new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				updateTriggerState(triggerKey);
			}
		};

		if (triggerState.equals("PAUSED")) {
			scheduleRequest.resumeTrigger(triggerKey).fire(receiver);
		} else {
			scheduleRequest.pauseTrigger(triggerKey).fire(receiver);
		}
	}

	@Override
	public void onCancel() {
		placeController().goTo(new AnalyticsTaskExecPlace());
	}
}