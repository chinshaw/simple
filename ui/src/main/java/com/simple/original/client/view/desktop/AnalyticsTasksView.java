package com.simple.original.client.view.desktop;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.simple.original.api.domain.GroupMembership;
import com.simple.original.api.domain.RecordFecthType;
import com.simple.original.client.proxy.AnalyticsTaskProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IAnalyticsTaskView;
import com.simple.original.client.view.widgets.ErrorPanel;
import com.simple.original.client.view.widgets.SortableColumn;

public class AnalyticsTasksView extends AbstractView implements IAnalyticsTaskView {

	/**
	 * FastforwardRows size to the simplepager constructor
	 */
	private static int DEFAULT_FAST_FORWARD_ROWS = 1000;

	/**
	 * This is the uibinder and it will use the view.DefaultView.ui.xml
	 * template.
	 */
	@UiTemplate("AnalyticsTasksView.ui.xml")
	public interface Binder extends UiBinder<Widget, AnalyticsTasksView> {
	}

	/**
	 * Page size for the number of tasks showing on table.
	 */
	private final int PAGE_SIZE = 15;

	/**
	 * Add script button.
	 */
	@UiField
	Button newAnalyticsTask;

	/**
	 * Used to delete selected analytics tasks.
	 */
	@UiField
	Button deleteSelectedAnalyticsTasks;

	/**
	 * Used to copy selected analytics tasks.
	 */
	@UiField
	Button copyAnalyticsTask;

	/**
	 * Used to copy selected analytics tasks.
	 */
	@UiField
	Button exportAnalyticsTasks;

	/**
	 * Used to copy selected analytics tasks.
	 */
	@UiField
	Button importAnalyticsTasks;

	/**
	 * Error panel for displaying errors.
	 */
	@UiField
	ErrorPanel errorPanel;

	/**
	 * Table containing the list of scripts.
	 */
	@UiField
	CellTable<AnalyticsTaskProxy> analyticsTasksTable;

	/**
	 * Pagination for displaying the list of alerts.
	 */
	@UiField
	SimplePager pager;

	/**
	 * Textbox for displaying the list of alerts based on search.
	 */
	@UiField
	TextBox searchQueryInput;

	/**
	 * Used to search text on click of this button
	 */

	@UiField
	Button searchResults;

	/**
	 * edit script button.
	 */
	@UiField
	Button editAnalyticsTask;

	/**
	 * Used to show list of options which admin can choose
	 */

	@UiField
	ListBox recordList = new ListBox();

	private Presenter presenter;

	private GroupMembership userRole;

	@Inject
	public AnalyticsTasksView(Resources resources) {
		super(resources);
		pager = new SimplePager();
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		setupSimplePager();
		initHandlers();
	}
	
	
	private void initHandlers() {
		searchQueryInput.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				search();
				//if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				//	search();
				//}
			}
		});
	}

	/**
	 * This method is called to set view for Admin/user in bind to view .

	@Override
	public void setUserView(GroupMembership userGroup) {
		userRole = userGroup;
		recordList.clear();
		if (userRole == GroupMembership.ADMINISTRATOR) { // Admin View for Task
			for (RecordFecthType type : RecordFecthType.values()) {
				recordList.addItem(type.getDescription() + appendText, type.name());
			}
			// default selection should be All Tasks
			recordList.setSelectedIndex(RecordFecthType.ALL_RECORDS.ordinal());
		} else { // User View for Task
			recordList.addItem(RecordFecthType.PUBLIC_RECORDS.getDescription() + appendText, RecordFecthType.PUBLIC_RECORDS.name());
			recordList.addItem(RecordFecthType.MY_RECORDS.getDescription() + appendText, RecordFecthType.MY_RECORDS.name());
			// default selection should be My Tasks
			recordList.setSelectedIndex(1);

		}
		setTaskView();
		addRecordListHandler();
	}
		 */

	/**
	 * This method is used to set the task View for Admin and User
	 */
	private void setTaskView() {
		recordList.setVisible(true);
		
		String recordSelected = recordList.getValue(recordList.getSelectedIndex());
		if (userRole == GroupMembership.ADMINISTRATOR) { // Admin View for Task
			exportAnalyticsTasks.setVisible(true);
			importAnalyticsTasks.setVisible(true);
			copyAnalyticsTask.setVisible(true);

		} else { // User View for Task
			boolean enableValue = false;
			if (recordSelected.equals(RecordFecthType.PUBLIC_RECORDS.name())) {
				enableValue = false;
			} else if (recordSelected.equals(RecordFecthType.MY_RECORDS.name())) {
				enableValue = true;
			}
			setEnabledEditTask(enableValue);
			setEnabledNewTask(enableValue);
			setEnabledImport(enableValue);
			setEnabledExport(enableValue);
		}
		setEnabledDeleteTask(false);
		setEnabledCopyTask(false);
		setEnabledEditTask(false);
	}

	/**
	 * This method is used to handle change of list item selected in the drop
	 * down listBox
	 */
	public void addRecordListHandler() {
		recordList.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				errorPanel.clear();
				searchQueryInput.setText("");
				setTaskView();
				presenter.updateRecordType(recordList.getValue(recordList.getSelectedIndex()));
			}
		});
	}

	@Override
	public ErrorPanel getErrorPanel() {
		return errorPanel;
	}

	/**
	 * This method is called in AnalyticsTasksViewImpl for displaying of
	 * pagination
	 */
	public void setupSimplePager() {
		pager.setDisplay(analyticsTasksTable);
	}

	/**
	 * UiFactory constructor for the simplepager.
	 * 
	 * @return
	 */
	@UiFactory
	SimplePager createSimplePager() {
		// Constructor changed for enabling the lastpage icon in pagination
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		final SimplePager alertsPager = new SimplePager(TextLocation.LEFT, pagerResources, false, DEFAULT_FAST_FORWARD_ROWS, true);
		return alertsPager;
	}

	/**
	 * This is used to draw the cell table.
	 * 
	 * @return CellTable for analytics tasks
	 */
	@UiFactory
	CellTable<AnalyticsTaskProxy> createTable() {
		final CellTable<AnalyticsTaskProxy> analyticsTasksTable = new CellTable<AnalyticsTaskProxy>(PAGE_SIZE, getResources());
		analyticsTasksTable.setEmptyTableWidget(new Label("No analytics tasks found"));

		CheckboxCell cb = new CheckboxCell(true, true);

		Column<AnalyticsTaskProxy, Boolean> selectorColumn = new Column<AnalyticsTaskProxy, Boolean>(cb) {

			@Override
			public Boolean getValue(AnalyticsTaskProxy analyticsTask) {
				return analyticsTasksTable.getSelectionModel().isSelected(analyticsTask);
			}
		};
		analyticsTasksTable.setColumnWidth(selectorColumn, "5em");

		selectorColumn.setFieldUpdater(new FieldUpdater<AnalyticsTaskProxy, Boolean>() {
			public void update(int index, AnalyticsTaskProxy object, Boolean value) {
				presenter.onCheckBoxClicked(value);
				analyticsTasksTable.getSelectionModel().setSelected(object, value);
			}
		});

		// Overriding the on browser event so that we can tell our activity that
		// select all has been clicked.
		CheckboxCell selectAll = new CheckboxCell(true, true) {
			@Override
			public void onBrowserEvent(Context context, Element parent, Boolean value, NativeEvent event, ValueUpdater<Boolean> valueUpdater) {
				// super.onBrowserEvent(context, parent, value, event,
				// valueUpdater);
				InputElement input = parent.getFirstChild().cast();
				Boolean isChecked = input.isChecked();
				presenter.onSelectAll(isChecked);
			}
		};
		analyticsTasksTable.addColumn(selectorColumn, new Header<Boolean>(selectAll) {

			@Override
			public Boolean getValue() {
				return false;
			}
		});
		
        // Operation ID
		SortableColumn<AnalyticsTaskProxy, Number> idColumn = new SortableColumn<AnalyticsTaskProxy, Number>(new NumberCell(NumberFormat.getFormat("#"))) {

			@Override
			public Number getValue(AnalyticsTaskProxy object) {
				return object.getId();
			}

			@Override
			public String getColumnSortKey() {
				return "id";
			}
        };
        
        analyticsTasksTable.addColumn(idColumn, "Task ID");

		// ID column
		Column<AnalyticsTaskProxy, String> nameColumn = new Column<AnalyticsTaskProxy, String>(new TextCell()) {

			@Override
			public String getValue(AnalyticsTaskProxy analyticsTask) {
				return analyticsTask.getName();
			}
		};
		nameColumn.setSortable(true);

		analyticsTasksTable.addColumn(nameColumn, "Analytics Task Name", "");

		// ID column
		Column<AnalyticsTaskProxy, String> description = new Column<AnalyticsTaskProxy, String>(new TextCell()) {

			@Override
			public String getValue(AnalyticsTaskProxy analyticsTask) {
				return analyticsTask.getDescription();
			}
		};
		description.setSortable(true);
		analyticsTasksTable.addColumn(description, "Analytics Task Description", "");

		Column<AnalyticsTaskProxy, String> ownerColumn = new Column<AnalyticsTaskProxy, String>(new TextCell()) {

			@Override
			public String getValue(AnalyticsTaskProxy analyticsTask) {
				try {

					return (analyticsTask.getOwner() != null) ? analyticsTask.getOwner().getName() : "Unknown User";

				} catch (NullPointerException e) {
					return "Unknown";
				}
			}
		};

		ownerColumn.setSortable(true);
		analyticsTasksTable.addColumn(ownerColumn, "Owner");

		// Visibility
		Column<AnalyticsTaskProxy, String> visibilityColumn = new Column<AnalyticsTaskProxy, String>(new TextCell()) {

			@Override
			public String getValue(AnalyticsTaskProxy analytics) {
				if (analytics.isPublic()) {
					return "Public";
				} else {
					return "Private";
				}
			}
		};
		visibilityColumn.setSortable(true);
		analyticsTasksTable.addColumn(visibilityColumn, "Visibility", "");

		return analyticsTasksTable;
	}

	/**
	 * Return the CellTable.
	 */
	@Override
	public CellTable<AnalyticsTaskProxy> getAnalyticsTasksTable() {
		return analyticsTasksTable;
	}

	/**
	 * Function used to reset the display. Clear out any handlers and such.
	 */
	@Override
	public void reset() {
		errorPanel.clear();
		searchQueryInput.setText("");
		recordList.clear();
		setEnabledNewTask(true);
		setEnabledImport(true);
		setEnabledDeleteTask(false);
		setEnabledExport(true);
		setEnabledCopyTask(false);
		setEnabledEditTask(false);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@UiHandler("deleteSelectedAnalyticsTasks")
	void onDeleteAnalyticsTasks(ClickEvent clickEvent) {
		errorPanel.clear();
		presenter.onDeleteTasks();
	}

	@UiHandler("copyAnalyticsTask")
	void onCopyAnalyticsTask(ClickEvent clickEvent) {
		errorPanel.clear();
		presenter.onCopyAnalyticsTask();
	}

	@UiHandler("newAnalyticsTask")
	void onNewAnalyticsTask(ClickEvent clickEvent) {
		errorPanel.clear();
		presenter.onNewTask();
	}

	@UiHandler("exportAnalyticsTasks")
	void onExportAnalyticsTasks(ClickEvent clickEvent) {
		errorPanel.clear();
		presenter.onExportAnalyticsTasks();
	}

	@UiHandler("importAnalyticsTasks")
	void importAnalyticsTasks(ClickEvent clickEvent) {
		errorPanel.clear();
		presenter.onImportAnalyticsTasks();
	}

	@UiHandler("editAnalyticsTask")
	void onEditAnalyticsTask(ClickEvent clickEvent) {
		errorPanel.clear();
		presenter.onEditTask();
	}

	@UiHandler("searchResults")
	void searchTask(ClickEvent clickEvent) {
		errorPanel.clear();
	}
	
	
	void search() {
		presenter.onClickSearch();
		setEnabledCopyTask(false);
		setEnabledDeleteTask(false);
		setEnabledEditTask(false);
	}

	@Override
	public TextBox getSearchText() {
		return searchQueryInput;
	}

	/**
	 * Method for implementing sort functionality
	 */
	public HandlerRegistration addColumnSortHandler(ColumnSortEvent.Handler handler) {
		return analyticsTasksTable.addColumnSortHandler(handler);
	}

	/**
	 * Method to set visibility of new Analytics Task button
	 */
	public void setEnabledNewTask(Boolean value) {
		newAnalyticsTask.setEnabled(value);
	}

	/**
	 * Method to set visibility of export Analytics Task button
	 */
	public void setEnabledExport(Boolean value) {
		exportAnalyticsTasks.setEnabled(value);
	}

	/**
	 * Method to set visibility of delete Analytics Task button
	 */
	@Override
	public void setEnabledDeleteTask(Boolean value) {
		if (userRole != null && userRole == GroupMembership.USER && recordList.getSelectedIndex() >= 0
				&& recordList.getValue(recordList.getSelectedIndex()).equals(RecordFecthType.PUBLIC_RECORDS.name())) {
			deleteSelectedAnalyticsTasks.setEnabled(false);
		} else {
			deleteSelectedAnalyticsTasks.setEnabled(value);
		}
	}

	/**
	 * Method to set visibility of import Analytics Task button
	 */
	public void setEnabledImport(Boolean value) {
		importAnalyticsTasks.setEnabled(value);
	}

	@Override
	public void setEnabledCopyTask(boolean value) {
		copyAnalyticsTask.setEnabled(value);
	}

	/**
	 * Method to set visibility of Edit Analytics Task button
	 */
	@Override
	public void setEnabledEditTask(Boolean value) {
		if (userRole != null && userRole == GroupMembership.USER && recordList.getSelectedIndex() >= 0
				&& recordList.getValue(recordList.getSelectedIndex()).equals(RecordFecthType.PUBLIC_RECORDS.name())) {
			editAnalyticsTask.setEnabled(false);
		} else {
			editAnalyticsTask.setEnabled(value);
		}
	}


	@Override
	public int getColumnIndex(Column<AnalyticsTaskProxy, ?> column) {
		return 0;
	}

}
