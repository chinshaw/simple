package com.simple.original.client.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.api.domain.RecordFecthType;
import com.simple.original.api.domain.SortOrder;
import com.simple.original.client.events.NotificationEvent;
import com.simple.original.client.place.SubscriptionPlace;
import com.simple.original.client.proxy.PreferencesProxy;
import com.simple.original.client.proxy.SubscriptionProxy;
import com.simple.original.client.proxy.SubscriptionProxy.SubscriptionType;
import com.simple.original.client.view.ISubscriptionView;

public class SubscriptionActivity extends AbstractActivity<SubscriptionPlace, ISubscriptionView> implements ISubscriptionView.Presenter {

	private SubscriptionType subscriptionType = null;
	private boolean updateInProgress = false;
	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(SubscriptionActivity.class.getName());

	/**
	 * List of the reportsIds/AlertIds which user wants to be subscribe
	 */
	private List<Long> toBeSubscribedIdList = new ArrayList<Long>();
	/**
	 * List of the reportsIds/AlertIds which user wants to be unsubscribe
	 */
	private List<Long> toBeUnSubscribedIdList = new ArrayList<Long>();

	@Inject
	public SubscriptionActivity(ISubscriptionView view) {
		super(view);
		subscriptionType = SubscriptionType.ALERT_DEFINITION;
	}
	

	/**
     * This is the asynchronous  data provider used to fetch reports/alerts
     */
	protected DataProvider dataProvider = new DataProvider();

	/**
	 * Bind view widgets to activity.
	 */
	@Override
	protected void bindToView() {
		display.getErrorPanel().clear();
		// Bind the view to the presenter
		display.setPresenter(this);
		// Disabling the Cancel/Save Button.
		display.getCancelSubscriptions().setEnabled(false);
		display.getSaveSubscriptions().setEnabled(false);
		dataProvider.addDataDisplay(display.getSubscriptionsTable());
		display.addColumnSortHandler(dataProvider);

	}

	/**
	 * The {@link ListDataProvider} used for user lists. This will fetch alerts/reports based on current place
	 * from the server and update the table accordingly.
	 */
	private class DataProvider extends ListDataProvider<SubscriptionProxy>
			implements ColumnSortEvent.Handler {

		private String sortColumn = null;
		private SortOrder sortOrder = SortOrder.DESCENDING;

		@Override
		protected void onRangeChanged(HasData<SubscriptionProxy> view) {

			final int startRange = display.getSubscriptionsTable().getVisibleRange().getStart();
			final int endRange = display.getSubscriptionsTable().getVisibleRange().getLength();
			if (updateInProgress) {
				return;
			}
			final Range range = display.getSubscriptionsTable().getVisibleRange();
			if (subscriptionType == SubscriptionType.ALERT_DEFINITION) {
				dao().alertSubscriptionRequest().getSubscriptions(startRange,endRange,RecordFecthType.PUBLIC_RECORDS,null, null, sortColumn,sortOrder).fire(new Receiver<List<SubscriptionProxy>>() {
					@Override
					public void onSuccess(List<SubscriptionProxy> response) {
						updateInProgress = false;
						if (response.size() < range.getLength()) {
							display.getSubscriptionsTable().setRowCount(range.getStart() + response.size() , true);
						}
						updateRowData(startRange,response);
						logger.info("SubscriptionActivity ->getRange() ALERT_DEFINITION Response ->startRange -> "+ startRange+ "  , response Length -> "+ response.size());
					}

					public void onFailure(ServerFailure error) {
						updateInProgress = false;
						setList(new ArrayList<SubscriptionProxy>());
						logger.info("SubscriptionActivity ->onRangeChanged ->ALERT_DEFINITION  Unable to retrieve Alert Names and Alert Descriptions "+ error.getMessage());
						display.showError("Unable to retrieve Alert Names and Alert Descriptions ",error);
					}
				});
			}

			if (subscriptionType == SubscriptionType.REPORT_TASK) {

				/*
				dao().reportTaskRequest().getSubscriptions(startRange, endRange,sortColumn, sortOrder).fire(new Receiver<List<SubscriptionProxy>>() {
					@Override
					public void onSuccess(List<SubscriptionProxy> response) {
						updateInProgress = false;
						if (response.size() < range.getLength()) {
							display.getSubscriptionsTable().setRowCount(range.getStart() + response.size() , true);
						}
						updateRowData(startRange, response);
						logger.info("SubscriptionActivity ->getRange() REPORT_TASK Response ->startRange -> "+ startRange+ "  , response Length -> "+ response.size());
					}

					public void onFailure(ServerFailure error) {
						updateInProgress = false;
						setList(new ArrayList<SubscriptionProxy>());
						logger.info("SubscriptionActivity ->onRangeChanged ->REPORT_TASK  Unable to retrieve Report Names and Report Descriptions "+ error.getMessage());
						display.showError("Unable to retrieve Report Names and Report Descriptions ",error);
					}
				});
				*/
			}
		}

		@Override
		public void onColumnSort(ColumnSortEvent event) {
			if (event.getColumn().isSortable()) {
				@SuppressWarnings("unchecked")
				int columnIndex = display.getColumnIndex((Column<SubscriptionProxy, ?>) event.getColumn());
				switch (columnIndex) {
				case 0:
					sortColumn = "name";
					sortOrder = (event.isSortAscending()) ? SortOrder.ASCENDING: SortOrder.DESCENDING;
					break;
				case 1:
					sortColumn = "description";
					sortOrder = (event.isSortAscending()) ? SortOrder.ASCENDING: SortOrder.DESCENDING;
					break;
				}
				logger.info("Selected sort : " + columnIndex + "-"
						+ sortOrder.asJPQL());
				display.getSubscriptionsTable().setVisibleRangeAndClearData(display.getSubscriptionsTable().getVisibleRange(), true);
			}
		}
	}

	/**
	 * This method is called on the check box Selection/Deselection.
	 */

	@Override
	public void onCheckBoxClicked(SubscriptionProxy object, Boolean isChecked,int index) {
		
		boolean result = false;
		if(isChecked){

			for(Long idValue : toBeUnSubscribedIdList){
				logger.info("check Box selected -> toBeUnSubscribedIdList"+idValue+" object.getId()->"+object.getId());
				if(idValue == object.getId()){
					toBeUnSubscribedIdList.remove(idValue);
					result = true;
					break;
				}
			}
			if(!result){
				toBeSubscribedIdList.add(object.getId());
				logger.info("check box selected"+object.getId());
			}

		}
		else{
			for(Long idValue : toBeSubscribedIdList){
				logger.info("check Box deselected -> toBeSubscribedIdList"+idValue+" object.getId()->"+object.getId());
				if(idValue == object.getId()){
					toBeSubscribedIdList.remove(idValue);
					result = true;
					break;
				}
			}
			if(!result){
				toBeUnSubscribedIdList.add(object.getId());
				logger.info("check box deselected"+object.getId());
			}
		}
		 if(toBeSubscribedIdList.isEmpty() && toBeUnSubscribedIdList.isEmpty()){
				//Disabling the Cancel/Save Button.
				display.getCancelSubscriptions().setEnabled(false);
				display.getSaveSubscriptions().setEnabled(false);
		 }else{
			//Enabling the Cancel/Save Button.
				display.getCancelSubscriptions().setEnabled(true);
				display.getSaveSubscriptions().setEnabled(true);
		 }
	}

	/**
	 * this method is used to save subscriptions
	 */
	@Override
	public void onSaveSubscriptions() {
		display.getSaveSubscriptions().setEnabled(false);
		display.getCancelSubscriptions().setEnabled(false);
		PreferencesProxy prefProxy = currentPerson().getPreferences();
		
		if(subscriptionType == SubscriptionType.ALERT_DEFINITION){
		
				if(prefProxy.getEmailFlag() || prefProxy.getSmsFlag()){
			    dao().alertSubscriptionRequest().saveAlertSubscriptions(toBeSubscribedIdList,toBeUnSubscribedIdList).fire(new Receiver<Void>() {
	
					@Override
					public void onSuccess(Void response) {
						        NotificationEvent ne = new NotificationEvent("Saved Successfully");
						        eventBus().fireEvent(ne);
						        toBeSubscribedIdList.clear();
						        toBeUnSubscribedIdList.clear();
						
					}
					@Override
					public void onFailure(ServerFailure error)
					{
						       display.getCancelSubscriptions().setEnabled(true);
						       display.getSaveSubscriptions().setEnabled(true);
						       logger.info("Couldn't Save new subscriptions error is "+ error.getMessage());
					}
				});
			}
				else{
					display.showError("Please set your Alert Notification Preferences in User Preferences screen to continue the subscription.");
				}
		}

	}

	/**
	 * this method is used to cancel the changes made to subscriptions
	 */
	@Override
	public void onCancelSubscriptions() {
		display.getSubscriptionsTable().setVisibleRangeAndClearData(display.getSubscriptionsTable().getVisibleRange(), true);
		display.getCancelSubscriptions().setEnabled(false);
		display.getSaveSubscriptions().setEnabled(false);

	}

}
