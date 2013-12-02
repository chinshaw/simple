package com.simple.original.client.activity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.api.analytics.IPerson;
import com.simple.original.api.domain.RecordFecthType;
import com.simple.original.api.domain.SortOrder;
import com.simple.original.client.events.NotificationEvent;
import com.simple.original.client.place.AnalyticsOperationsPlace;
import com.simple.original.client.place.CopyOperationBuilderPlace;
import com.simple.original.client.place.CreateEditOperationBuilderPlace;
import com.simple.original.client.proxy.AnalyticsOperationProxy;
import com.simple.original.client.proxy.AnalyticsTaskProxy;
import com.simple.original.client.service.BaseListDataProvider;
import com.simple.original.client.service.DaoRequestFactory.DaoRequest;
import com.simple.original.client.view.IOperationsView;
import com.simple.original.client.view.IOperationsView.Presenter;
import com.simple.original.client.view.widgets.AnalyticsPopupPanel;

public class AnalyticsOperationsActivity extends AbstractActivity<AnalyticsOperationsPlace, IOperationsView> implements Presenter {

	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(AnalyticsOperationsActivity.class.getName());

	private class AnalyticsDataProvider extends BaseListDataProvider<AnalyticsOperationProxy> {
		private String sortColumn = null;
		private SortOrder sortOrder = SortOrder.DESCENDING;
		private final String NAME_COLUMN = "name";
		private final String DESCRIPTION_COLUMN = "description";
		private final String PUBLIC_OWNER_COLUMN = "owner";

		@Override
		protected CellTable<AnalyticsOperationProxy> getDisplayTable() {
			return display.getAnalyticsTableData();
		}

		@Override
		protected DaoRequest<AnalyticsOperationProxy> getRequestProvider() {
			return dao().createAnalyticsOperationRequest();
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

		@Override
		public String[] getWithProperties() {
			return new String[] {"owner"};
		}

		@Override
		protected String getSearchColumn() {
			return NAME_COLUMN;
		}

		/**
		 * This method is used to sort the Analytics operation on click of the
		 * column header.
		 */
		@Override
		public void onColumnSort(ColumnSortEvent event) {
			if (event.getColumn().isSortable()) {
				@SuppressWarnings("unchecked")
				int columnIndex = display.getColumnIndex((Column<AnalyticsOperationProxy, ?>) event.getColumn());

				switch (columnIndex) {
				case 1:
					sortColumn = NAME_COLUMN;
					sortOrder = (event.isSortAscending()) ? SortOrder.ASCENDING : SortOrder.DESCENDING;
					break;
				case 2:
					sortColumn = DESCRIPTION_COLUMN;
					sortOrder = (event.isSortAscending()) ? SortOrder.ASCENDING : SortOrder.DESCENDING;
					break;
				case 4: // Sorting is not required on Operation type(Index 3) so
						// we sort on index 4
					sortColumn = PUBLIC_OWNER_COLUMN;
					sortOrder = (event.isSortAscending()) ? SortOrder.ASCENDING : SortOrder.DESCENDING;
					break;
				}
				logger.info("Selected sort : " + columnIndex + "-" + sortOrder.asJPQL());
				display.getAnalyticsTable().setVisibleRangeAndClearData(display.getAnalyticsTable().getVisibleRange(), true);
			}
		}
	}

	/**
	 * This is the variable used to get the List item selected by the Current
	 * User
	 */
	private RecordFecthType fetchtype = null;

	/**
	 * This is the analytics operation data provider used to fetch the analytics
	 * operation and updated when needed.
	 */
	private AnalyticsDataProvider dataProvider = new AnalyticsDataProvider();

	/**
	 * Selection model to handle the table selections.
	 */
	private MultiSelectionModel<AnalyticsOperationProxy> selectionModel = new MultiSelectionModel<AnalyticsOperationProxy>();

	@Inject
	public AnalyticsOperationsActivity(IOperationsView view) {
		super(view);
	}

	@Override
	protected void bindToView() {
		display.setPresenter(this); // Bind the view to the presenter
		dataProvider.addDataDisplay(display.getAnalyticsTable());
		display.getAnalyticsTable().setSelectionModel(selectionModel);
		selectionModel.clear();
		display.addColumnSortHandler(dataProvider); // Column Sorting
	}

	@Override
	public void onAddAnalytics() {
		placeController().goTo(new CreateEditOperationBuilderPlace(null));
	}

	@Override
	public void onDeleteAnalyticsOperations() {
		final Set<AnalyticsOperationProxy> selectedAnalyticsOperation = selectionModel.getSelectedSet();
		display.setEnabledDeleteButton(false);
		final Set<Long> idsToDelete = new HashSet<Long>();
		for (AnalyticsOperationProxy analyticsOperation : selectedAnalyticsOperation) {
			idsToDelete.add(analyticsOperation.getId());
		}
		dao().createAnalyticsTaskRequest().getAnalyticsTasksForOperationIds(idsToDelete).fire(new Receiver<List<AnalyticsTaskProxy>>() {

			@Override
			public void onSuccess(List<AnalyticsTaskProxy> tasks) {
				if (!tasks.isEmpty()) {
					// Show popup panel to display all the linked Tasks
					// associated with the selected operations.
					AnalyticsPopupPanel analyticsPopupPanel = new AnalyticsPopupPanel(false);
					analyticsPopupPanel.setMessage("Selected " + idsToDelete.size() + " Operation(s) has the following linked Task(s) :");
					int index = 1;
					for (AnalyticsTaskProxy task : tasks) {
						analyticsPopupPanel.setMessage(index + ". " + task.getName());
						index++;
					}
					analyticsPopupPanel.setMessage("Can not delete the operation(s), please remove the linked task(s)");
					analyticsPopupPanel.showPopup();
					display.setEnabledDeleteButton(true);
				} else {
					boolean confirm = Window.confirm("Are you sure you want to delete the " + selectedAnalyticsOperation.size() + " analytics operations");
					if (confirm) {
						dao().createAnalyticsOperationRequest().deleteList(idsToDelete).fire(new Receiver<Integer>() {
							@Override
							public void onSuccess(Integer numberOfEntitiesDeleted) {
								if (numberOfEntitiesDeleted != idsToDelete.size()) {
									display.showError("Unable to delete all the selected operations");
								} else {
									NotificationEvent ne = new NotificationEvent("Deleted Successfully");
									eventBus().fireEvent(ne);
								}
								display.getAnalyticsTable().setVisibleRangeAndClearData(display.getAnalyticsTable().getVisibleRange(), true);
								display.disableCopyEditDeleteButtons();
								selectionModel.clear();
								display.setEnabledDeleteButton(true);
							}

							public void onFailure(ServerFailure error) {
								display.setEnabledDeleteButton(true);
								logger.info("AlertOperationActivity->onDeleteAnalyticsOperations->deleteList():" + error.toString());
								display.showError("Unable to delete Operation(s) ", error);
							}

						});
					} else {
						display.setEnabledDeleteButton(true);
					}
				}
			}

			public void onFailure(ServerFailure error) {
				display.setEnabledDeleteButton(true);
				logger.info("AlertOperationActivity->onDeleteAnalyticsOperations->getTasksByOperation():" + error.toString());
				display.showError("Unable to get linked tasks: ", error);
			}
		});
	}

	@Override
	public void onEditAnalytics() {
		final Set<AnalyticsOperationProxy> selectedAnalyticsOperation = selectionModel.getSelectedSet();
		for (AnalyticsOperationProxy analyticsOperationProxy : selectedAnalyticsOperation) {
			placeController().goTo(new CreateEditOperationBuilderPlace(analyticsOperationProxy.getId()));
			selectionModel.clear();
			break;
		}
	}

	@Override
	public void onCopyAnalytics() {
		final Set<AnalyticsOperationProxy> selectedAnalyticsOperation = selectionModel.getSelectedSet();
		for (AnalyticsOperationProxy analyticsOperationProxy : selectedAnalyticsOperation) {
			logger.info("operation id is " + analyticsOperationProxy.getId());
			placeController().goTo(new CopyOperationBuilderPlace(analyticsOperationProxy.getId()));
			selectionModel.clear();
			break;
		}
	}

	/**
	 * this method is called on change of the Operation list item this will
	 * reload the data of cell table according to the selection of the ListBox
	 * item
	 */
	@Override
	public void onOperationSelected(String recordSelected) {
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
		display.getAnalyticsTableData().setVisibleRangeAndClearData(display.getAnalyticsTableData().getVisibleRange(), true);
	}

	/**
	 * This method is called on click of search button to load the data on
	 * celltable with the search results
	 */
	@Override
	public void onClickSearch() {
		selectionModel.clear();
		display.getAnalyticsTableData().setVisibleRangeAndClearData(display.getAnalyticsTableData().getVisibleRange(), true);
		display.disableCopyEditDeleteButtons();
	}

	@Override
	public void onSelectAll(Boolean selectAll) {
		Iterable<AnalyticsOperationProxy> selectable = display.getAnalyticsTable().getVisibleItems();
		for (Iterator<AnalyticsOperationProxy> iter = selectable.iterator(); iter.hasNext();) {
			selectionModel.setSelected(iter.next(), selectAll);
		}
		display.setEnabledCopyButton(false);
		display.setEnabledEditButton(false);
		display.setEnabledDeleteButton(selectAll);
	}

	@Override
	public void onCancel() {
		display.getSearchText().setText("");
		placeController().goTo(new AnalyticsOperationsPlace());
	}

	@Override
	public void onCheckBoxSelected(AnalyticsOperationProxy object, Boolean value) {
		// value will be added to selectedList only after onCheckBoxSelected()
		// returns
		int selectedItems = ((MultiSelectionModel<AnalyticsOperationProxy>) selectionModel).getSelectedSet().size();
		if (value == true) {
			// when check box value is selected
			// we need to increase selectedItems by 1
			selectedItems += 1;
		} else {
			// when check box value is de-selected
			// we need to decrease selectedItems by 1
			selectedItems -= 1;
		}
		display.setEnabledEditButton(selectedItems == 1);
		display.setEnabledCopyButton(selectedItems == 1);
		display.setEnabledDeleteButton(selectedItems != 0);
	}
}