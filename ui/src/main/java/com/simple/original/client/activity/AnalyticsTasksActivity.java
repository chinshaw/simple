package com.simple.original.client.activity;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.inject.Inject;
import com.simple.original.api.domain.RecordFecthType;
import com.simple.original.api.domain.SortOrder;
import com.simple.original.client.place.AnalyticsTaskBuilderPlace;
import com.simple.original.client.place.AnalyticsTaskCopyPlace;
import com.simple.original.client.place.TasksPlace;
import com.simple.original.client.proxy.AnalyticsTaskProxy;
import com.simple.original.client.service.DaoBaseDataProvider;
import com.simple.original.client.service.DaoRequestFactory.DaoRequest;
import com.simple.original.client.view.IAnalyticsTaskView;
import com.simple.original.client.view.widgets.ImportScriptsPopup;

public class AnalyticsTasksActivity extends AbstractActivity<TasksPlace, IAnalyticsTaskView> implements IAnalyticsTaskView.Presenter {


	/**
	 * This is the variable used to get the List item selected by the Current
	 * User
	 */
	private RecordFecthType fetchtype = null;

	/**
	 * The AnalyticsDataProvider is used for fetching analytics tasks from the
	 * server and update the table accordingly.
	 */
	private class AnalyticsDataProvider extends DaoBaseDataProvider<AnalyticsTaskProxy> {

		@Override
		public String[] getWithProperties() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public DaoRequest<AnalyticsTaskProxy> getRequestProvider() {
			// TODO Auto-generated method stub
			return null;
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
		//display.addColumnSortHandler(analyticDataProvider);
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

}
