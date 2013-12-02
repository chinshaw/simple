package com.simple.original.client.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Request;
import com.simple.original.api.analytics.IPerson;
import com.simple.original.api.domain.RecordFecthType;
import com.simple.original.api.domain.SortOrder;
import com.simple.original.client.place.AnalyticsTaskBuilderPlace;
import com.simple.original.client.place.AnalyticsTaskCopyPlace;
import com.simple.original.client.place.ReportAdminSubscriptionPlace;
import com.simple.original.client.place.TasksPlace;
import com.simple.original.client.proxy.AnalyticsTaskMonitorProxy;
import com.simple.original.client.proxy.AnalyticsTaskProxy;
import com.simple.original.client.service.BaseListDataProvider;
import com.simple.original.client.service.DaoRequestFactory.DaoRequest;
import com.simple.original.client.view.IAnalyticsTaskView;
import com.simple.original.client.view.widgets.AnalyticsPopupPanel;
import com.simple.original.client.view.widgets.ImportScriptsPopup;

public class AnalyticsTasksActivity extends AbstractActivity<TasksPlace, IAnalyticsTaskView> implements IAnalyticsTaskView.Presenter {

	private String sortColumn = null;
	private SortOrder sortOrder = SortOrder.DESCENDING;
	private final String NAME_COLUMN = "name";
	private final String DESCRIPTION_COLUMN = "description";
	private final String PUBLIC_OWNER_COLUMN = "owner";

	/**
	 * This is the variable used to get the List item selected by the Current
	 * User
	 */
	private RecordFecthType fetchtype = null;

	/**
	 * The AnalyticsDataProvider is used for fetching analytics tasks from the
	 * server and update the table accordingly.
	 */
	class AnalyticsDataProvider extends BaseListDataProvider<AnalyticsTaskProxy> {

		@Override
		protected DaoRequest<AnalyticsTaskProxy> getRequestProvider() {
			return dao().createAnalyticsTaskRequest();
		}

		@Override
		protected CellTable<AnalyticsTaskProxy> getDisplayTable() {
			return display.getAnalyticsTasksTable();
		}

		@Override
		protected String getSearchText() {
			String searchText = display.getSearchText().getValue();
			if (searchText.contains("'")) {
				searchText = searchText.replaceAll("'", "''");
			}
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

		protected String[] getWithProperties() {
			return new String[] { "owner" };
		}

		@Override
		protected Request<List<AnalyticsTaskProxy>> getRecordsQuery(Range range) {
			return dao().createAnalyticsTaskRequest()
					.search(range.getStart(), range.getLength(), getRecordFecthType(), getSearchText(), getSearchColumn(), getSortColumn(), getSortOrder())
					.with(getWithProperties());
		}

		/**
		 * This method is used to sort the analytics tasks onclick of the column
		 * header.
		 */
		@Override
		public void onColumnSort(ColumnSortEvent event) {
			if (event.getColumn().isSortable()) {
				@SuppressWarnings("unchecked")
				int columnIndex = display.getColumnIndex((Column<AnalyticsTaskProxy, ?>) event.getColumn());

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
				display.getAnalyticsTasksTable().setVisibleRangeAndClearData(display.getAnalyticsTasksTable().getVisibleRange(), true);
			}
		}

	}

	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger("AnalyticsTasksActivity");

	/**
	 * This is the asynchronous task data provider used to fetch tasks and
	 * update the editor when needed.
	 */
	private AnalyticsDataProvider analyticDataProvider = new AnalyticsDataProvider();

	PopupPanel popupPanel;

	FlowPanel panel;

	/**
	 * Table selection model. This is what handles table selections.
	 */

	protected MultiSelectionModel<AnalyticsTaskProxy> analyticsSelectionModel = new MultiSelectionModel<AnalyticsTaskProxy>();

	@Inject
	public AnalyticsTasksActivity(IAnalyticsTaskView view) {
		super(view);
	}

	/**
	 * Bind view widgets to activity.
	 */
	@Override
	protected void bindToView() {
		// Bind the view to the presenter
		display.setPresenter(this);
		analyticDataProvider.addDataDisplay(display.getAnalyticsTasksTable());
		display.getAnalyticsTasksTable().setSelectionModel(analyticsSelectionModel);
		analyticsSelectionModel.clear();
		display.addColumnSortHandler(analyticDataProvider);
	}

	/**
	 * this is the method to check all CheckBoxes on click of SelectAll CheckBox
	 */
	@Override
	public void onSelectAll(Boolean selectAll) {
		Iterable<AnalyticsTaskProxy> selectable = display.getAnalyticsTasksTable().getVisibleItems();
		for (Iterator<AnalyticsTaskProxy> iter = selectable.iterator(); iter.hasNext();) {
			analyticsSelectionModel.setSelected(iter.next(), selectAll);
		}
		display.setEnabledCopyTask(false);
		display.setEnabledEditTask(false);
		display.setEnabledDeleteTask(selectAll);
	}

	/**
	 * This method is used to delete the selected Analytics/ Reporting Task
	 */
	@Override
	public void onDeleteTasks() {
		display.setEnabledDeleteTask(false);
		display.getErrorPanel().clear();
	}

	/**
	 * This method is used to go to Create AnalyticsTask/ReportTask screen where
	 * we can create a new Task
	 */
	@Override
	public void onNewTask() {
		placeController().goTo(new AnalyticsTaskBuilderPlace(null));
	}

	/**
	 * This method is used to copy the selected analyticsTask with respective
	 * operations
	 */

	@Override
	public void onCopyAnalyticsTask() {
		final Set<AnalyticsTaskProxy> selectedAnalyticsTasks = analyticsSelectionModel.getSelectedSet();

		for (AnalyticsTaskProxy analyticsTask : selectedAnalyticsTasks) {
			logger.info("onCopyAnalyticsTask ->" + analyticsTask.getId());
			placeController().goTo(new AnalyticsTaskCopyPlace(analyticsTask.getId()));
			analyticsSelectionModel.clear();
			break;
		}
	}

	/**
	 * This method is used to go to edit AnalyticsTask/ReportTask screen where
	 * we can Edit Task
	 */
	@Override
	public void onEditTask() {
		
	}

	/**
	 * This method is called on click of export button in analytics task screen
	 */
	public void onExportAnalyticsTasks() {
		Window.open(GWT.getHostPageBaseURL() + "/analyticsTaskRestore", "_blank", "enabled");
	}

	/**
	 * This method is called on click of Import button in analytics task screen
	 */
	public void onImportAnalyticsTasks() {
		ImportScriptsPopup exportPopup = new ImportScriptsPopup();
		exportPopup.center();
	}

	/**
	 * This method is called on click of search button to load the data on
	 * celltable with the search results
	 */
	@Override
	public void onClickSearch() {
		analyticsSelectionModel.clear();
		display.getAnalyticsTasksTable().setVisibleRangeAndClearData(display.getAnalyticsTasksTable().getVisibleRange(), true);
	}

	/**
	 * this method is called on change of the list item this will reload the
	 * data of celltable according to the selection of the ListBox item
	 */
	@Override
	public void updateRecordType(String recordSelected) {
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
		analyticsSelectionModel.clear();
		display.getAnalyticsTasksTable().setVisibleRangeAndClearData(display.getAnalyticsTasksTable().getVisibleRange(), true);
	}

	/**
	 * This method is used to display a popup for the tasks which are linked by
	 * alert definitions
	 */
	public void displayDeleteTasks(final List<AnalyticsTaskMonitorProxy> alerts, final Set<Long> taskIdList) {
		Map<String, List<String>> mapData = new HashMap<String, List<String>>();
		Set<String> taskNamesList = new HashSet<String>();

		for (AnalyticsTaskMonitorProxy alert : alerts) {
			taskNamesList.add(alert.getAnalyticsTask().getName());
			if (mapData.containsKey(alert.getAnalyticsTask().getName())) {
				List<String> alertNamesList = mapData.get(alert.getAnalyticsTask().getName());
				alertNamesList.add(alert.getName());
				mapData.put(alert.getAnalyticsTask().getName(), alertNamesList);
			} else {
				List<String> alertNames = new ArrayList<String>();
				alertNames.add(alert.getName());
				mapData.put(alert.getAnalyticsTask().getName(), alertNames);
			}
		}

		AnalyticsPopupPanel analyticsPopupPanel = new AnalyticsPopupPanel(false);
		analyticsPopupPanel.setMessage(" Cannot Delete as Alerts are Related to Tasks ");

		for (String key : mapData.keySet()) {
			List<String> alertNamesList = mapData.get(key);
			analyticsPopupPanel.setMessage("Alert(s) related to TaskName : \"" + key + "\"");
			for (String alertName : alertNamesList) {
				analyticsPopupPanel.setMessage("\"" + alertName + "\"");
			}
		}
		analyticsPopupPanel.showPopup();
		display.setEnabledDeleteTask(true);
	}

	/**
	 * This method is called on the check box Selection/Deselection.
	 */
	@Override
	public void onCheckBoxClicked(Boolean value) {
		int selectionSize = 0;
		// value will be added to selectedList only after onCheckBoxClicked()
		// returns
		selectionSize = ((MultiSelectionModel<AnalyticsTaskProxy>) analyticsSelectionModel).getSelectedSet().size();

		logger.info("selectionSize is --> " + selectionSize);
		if (value == true) {
			// when check box value is selected
			// we need to increase selectionSize by 1
			selectionSize += 1;
			display.setEnabledCopyTask(selectionSize == 1);
			display.setEnabledEditTask(selectionSize == 1);
			display.setEnabledDeleteTask(true);
		} else {
			// when check box value is de-selected
			// we need to reduce selectionSize by 1
			selectionSize -= 1;
			display.setEnabledDeleteTask(selectionSize != 0);
			display.setEnabledCopyTask(selectionSize == 1);
			display.setEnabledEditTask(selectionSize == 1);
		}
	}

	/**
	 * This method is used to redirect to report subscription screen when
	 * subscriptions link in report manager screen is clicked.
	 */
	@Override
	public void onSubscription(AnalyticsTaskProxy analyticsTask) {
		placeController().goTo(new ReportAdminSubscriptionPlace(analyticsTask.getId()));
	}
}
