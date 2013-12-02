package com.simple.original.client.activity;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.api.analytics.IPerson;
import com.simple.original.api.domain.RecordFecthType;
import com.simple.original.api.domain.SortOrder;
import com.simple.original.client.events.NotificationEvent;
import com.simple.original.client.place.AlertDefinitionEditPlace;
import com.simple.original.client.place.EmailNotificationPlace;
import com.simple.original.client.place.MonitoringPlace;
import com.simple.original.client.proxy.AnalyticsTaskMonitorProxy;
import com.simple.original.client.service.BaseListDataProvider;
import com.simple.original.client.service.DaoRequestFactory;
import com.simple.original.client.service.DaoRequestFactory.DaoRequest;
import com.simple.original.client.view.IAlertDefinitionView;

public class AlertDefinitionActivity extends AbstractActivity<MonitoringPlace, IAlertDefinitionView> implements IAlertDefinitionView.Presenter {

	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(AlertDefinitionActivity.class.getName());

	/**
	 * The {@link ListDataProvider} used for user lists. This will fetch alerts
	 * from the server and update the table accordingly.
	 */
	private class TableDataProvider extends BaseListDataProvider<AnalyticsTaskMonitorProxy> {

		private String sortColumn = null;
		private SortOrder sortOrder = SortOrder.DESCENDING;

		private final String NAME_COLUMN = "name";
		private final String DESCRIPTION_COLUMN = "description";
		private final String PUBLIC_OWNER_COLUMN = "owner";

		@Override
		protected CellTable<AnalyticsTaskMonitorProxy> getDisplayTable() {
			return display.getAlertsTable();
		}

		@Override
		protected DaoRequest<AnalyticsTaskMonitorProxy> getRequestProvider() {
			return daoRequestFactory.alertDefinitionRequest();
		}

		@Override
		protected String getSearchText() {
			String searchText = display.getSearchQueryInput().getValue();
			if (searchText.contains("'")) {
				searchText = searchText.replaceAll("'", "''");
			}
			((MultiSelectionModel<AnalyticsTaskMonitorProxy>) selectionModel).clear();
			return searchText;
		}

		@Override
		protected String getSearchColumn() {
			return NAME_COLUMN;
		}

		@Override
		protected String getSortColumn() {
			return sortColumn;
		}

		@Override
		protected SortOrder getSortOrder() {
			return sortOrder;
		}

		@Override
		protected String[] getWithProperties() {
			return AnalyticsTaskMonitorProxy.EDIT_PROPERTIES;
		}

		@Override
		protected void showError(String errorMessage) {
			display.showError(errorMessage);
		}

		@Override
		protected RecordFecthType getRecordFecthType() {
			if (fetchtype == null) {
				fetchtype = currentPerson().getRoles().contains(IPerson.Role.ADMIN) ? RecordFecthType.ALL_RECORDS : RecordFecthType.MY_RECORDS;
			}
			return fetchtype;
		}

		/**
		 * This method is used to sort the alerts onclick of the column header.
		 */
		@Override
		public void onColumnSort(ColumnSortEvent event) {

			if (event.getColumn().isSortable()) {
				@SuppressWarnings("unchecked")
				int columnIndex = display.getColumnIndex((Column<AnalyticsTaskMonitorProxy, ?>) event.getColumn());

				switch (columnIndex) {
				case 1:
					sortColumn = NAME_COLUMN;
					sortOrder = (event.isSortAscending()) ? SortOrder.ASCENDING : SortOrder.DESCENDING;
					break;
				case 2:
					sortColumn = DESCRIPTION_COLUMN;
					sortOrder = (event.isSortAscending()) ? SortOrder.ASCENDING : SortOrder.DESCENDING;
					break;
				case 3:
					sortColumn = PUBLIC_OWNER_COLUMN;
					sortOrder = (event.isSortAscending()) ? SortOrder.ASCENDING : SortOrder.DESCENDING;
					break;
				}
				logger.info("Selected sort : " + columnIndex + "-" + sortOrder.asJPQL());
				display.getAlertsTable().setVisibleRangeAndClearData(display.getAlertsTable().getVisibleRange(), true);
			}
		}

	}

	/**
	 * This is the asynchronous alert table data provider used to fetch data and
	 * update the editor when needed.
	 */
	protected TableDataProvider tableDataProvider = new TableDataProvider();

	/**
	 * This is the variable used to get the List item selected by the Current
	 * User
	 */
	private RecordFecthType fetchtype = null;

	/**
	 * Table selection model. This is what handles table selections.
	 */
	protected MultiSelectionModel<AnalyticsTaskMonitorProxy> selectionModel = new MultiSelectionModel<AnalyticsTaskMonitorProxy>();

	@Inject
	DaoRequestFactory daoRequestFactory;
	
	@Inject
	public AlertDefinitionActivity(IAlertDefinitionView view) {
		super(view);
	}

	/**
	 * Bind view widgets to activity.
	 */
	@Override
	protected void bindToView() {
		// Bind the view to the presenter
		display.setPresenter(this);

		tableDataProvider.addDataDisplay(display.getAlertsTable());
		display.getAlertsTable().setSelectionModel(selectionModel);
		((MultiSelectionModel<AnalyticsTaskMonitorProxy>) selectionModel).clear();

		display.getSelectedAlertsToDelete();
		display.addColumnSortHandler(tableDataProvider);
	}

	/**
	 * This method is called when user performs a delete operation on the
	 * alerts.
	 */
	@Override
	public void onDeleteAlerts() {
		final Set<AnalyticsTaskMonitorProxy> selectedAlerts = ((MultiSelectionModel<AnalyticsTaskMonitorProxy>) selectionModel).getSelectedSet();
		if (selectedAlerts.size() < 1) {
			display.showError("Please select alerts");
			return;
		} else {
			display.getErrorPanel().clear();
			boolean confirm = Window.confirm("Are you sure you want to delete the " + selectedAlerts.size() + " Alert(s)");

			if (confirm) {
				Set<Long> idsToDelete = new HashSet<Long>();
				for (AnalyticsTaskMonitorProxy alert : selectedAlerts) {
					idsToDelete.add(alert.getId());
				}
				daoRequestFactory.alertDefinitionRequest().deleteList(idsToDelete).fire(new Receiver<Integer>() {
					@Override
					public void onSuccess(final Integer response) {
						final NotificationEvent ne = new NotificationEvent("Deleted Successfully");
						eventBus().fireEvent(ne);
						logger.info("[AlertDefintionActivity:onDeleteAlerts()]");
						display.getAlertsTable().setVisibleRangeAndClearData(display.getAlertsTable().getVisibleRange(), true);
						// To Disable the Delete Button
						display.getSelectedAlertsToDelete().setEnabled(false);
						display.setEnabledEditButton(false);
						display.setEnabledNotifyButton(false);
						((MultiSelectionModel<AnalyticsTaskMonitorProxy>) selectionModel).clear();
					}

					public void onFailure(final ServerFailure error) {
						logger.info("AlertDefinitionActivity ->onDeleteAlerts -> Unable to delete Alert(s) " + error.toString());
						display.showError("Unable to delete Alert(s) ", error);
					}
				});
			}
		}
	}

	/**
	 * This method is called when user clicks on Add button in the alert
	 * management screen.
	 */
	@Override
	public void onNewAlert() {
		placeController().goTo(new AlertDefinitionEditPlace(null));
	}

	/**
	 * This method is called when user clicks on Edit button in the alert
	 * management screen.
	 */
	@Override
	public void onEditAlert() {
		final Set<AnalyticsTaskMonitorProxy> selectedAlerts = selectionModel.getSelectedSet();
		for (AnalyticsTaskMonitorProxy alert : selectedAlerts) {
			logger.info("onEditAlertDefinition ->" + alert.getId());
			placeController().goTo(new AlertDefinitionEditPlace(alert.getId()));
		}
	}

	/**
	 * This method is called when user clicks on Notify button in the alert
	 * management screen.
	 */
	@Override
	public void onNotifyAlert() {
		final Set<AnalyticsTaskMonitorProxy> selectedAlerts = selectionModel.getSelectedSet();
		for (AnalyticsTaskMonitorProxy alert : selectedAlerts) {
			logger.info("onEditAlertDefinition ->" + alert.getId());
			placeController().goTo(new EmailNotificationPlace(alert));
		}
	}

	/**
	 * To check the check box clicked Event.
	 */
	@Override
	public void onCheckBoxClicked(AnalyticsTaskMonitorProxy object, Boolean value) {

		int selectionSize = ((MultiSelectionModel<AnalyticsTaskMonitorProxy>) selectionModel).getSelectedSet().size();
		logger.info("selectionSize is --> " + selectionSize);
		if (value == true) {
			// when check box value is selected
			// we need to increase selectionSize by 1
			selectionSize += 1;
			display.setEnabledEditButton(selectionSize == 1);
			display.setEnabledNotifyButton(selectionSize == 1);
			display.setEnabledDeleteButton(true);
		} else {
			// when check box value is de-selected
			// we need to reduce selectionSize by 1
			selectionSize -= 1;
			display.setEnabledDeleteButton(selectionSize != 0);
			display.setEnabledNotifyButton(selectionSize == 1);
			display.setEnabledEditButton(selectionSize == 1);
		}
	}

	/**
	 * this method is called on change of the list item which Admin can select
	 * this will reload the data of celltable according to the Admin selection
	 * of the ListBox item
	 */
	@Override
	public void onAdminListSelected(String recordSelected) {
		logger.info("selected record Fetch type ->" + recordSelected);
		if (recordSelected.equals(RecordFecthType.ALL_RECORDS.name())) {
			fetchtype = RecordFecthType.ALL_RECORDS;
		} else if (recordSelected.equals(RecordFecthType.PUBLIC_RECORDS.name())) {
			fetchtype = RecordFecthType.PUBLIC_RECORDS;
		} else if (recordSelected.equals(RecordFecthType.USER_RECORDS.name())) {
			fetchtype = RecordFecthType.USER_RECORDS;
		} else if (recordSelected.equals(RecordFecthType.MY_RECORDS.name())) {
			fetchtype = RecordFecthType.MY_RECORDS;
		}
		selectionModel.clear();
		display.getAlertsTable().setVisibleRangeAndClearData(display.getAlertsTable().getVisibleRange(), true);
	}

}
