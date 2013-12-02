package com.simple.original.client.view.desktop;

import java.util.logging.Logger;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.simple.original.client.proxy.PersonProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IMonitoringTaskLimitView;
import com.simple.original.client.view.widgets.ErrorPanel;


public class MonitoringTaskLimitView extends AbstractView implements IMonitoringTaskLimitView {

    /*
     * Logger instance
     */
    private static final Logger logger = Logger.getLogger(MonitoringTaskLimitView.class.getName());

	private final int PAGE_SIZE = 5;

	/** Constants to use for displaying celltable columns */
	private static final String USER_NAME = "UserName";
	private static final String TASKLIMIT= "TaskLimit";
	private static final String EMAIL= "Email";
	private static final String CURRENTTASK= "Current Task(s)";


	/**
	 * This is the uibinder and it will use the NewMonitoringTaskLimitView.ui.xml
	 * template.
	 */
	@UiTemplate("MonitoringTaskLimitView.ui.xml")
	public interface Binder extends
			UiBinder<Widget, MonitoringTaskLimitView> {
	}

	/**
	 * defaultTaskLimit textbox
	 */
	@UiField
	TextBox value;

	/**
     * Textbox for displaying the list of users based on search.
     */
	@Ignore
    @UiField
    TextBox searchQueryInput;

    /**
     * Used to search text on click of this button
     */
    @UiField
    Button searchResults;

	/**
     * Pagination for displaying the list of exception users.
     */
    @UiField
    SimplePager pager;

	/**
	 * Table containing the list of exception users.
	 */
	@Ignore
	@UiField
	CellTable<PersonProxy> userTable;

	
	/**
	 * FastforwardRows size to the simplepager constructor
	 */
	private static int DEFAULT_FAST_FORWARD_ROWS = 1000;

	/**
	 * Button to save defaultTaskLimit
	 */
	@UiField
	Button save;

	/**
	 * ErrorPanel to show the errors
	 */
	@Ignore
	@UiField
	ErrorPanel errorPanel;

	/**
	 * Button to save the changes
	 */
	@UiField
	Button saveTaskLimit;

	/**
	 * Button to cancel the changes to the screen
	 */
	@UiField
	Button cancel;

	/**
	 * Presenter.
	 */
	private Presenter presenter;

	/**
	 * This is the monitoring tasklimitdriver
	 * 
	 */
	//private final MonitoringTaskLimitDriver driver = GWT.create(MonitoringTaskLimitDriver.class);

	/**
	 * Required default constructor
	 * @param resources
	 */
	@Inject
	public MonitoringTaskLimitView(Resources resources) {
		super(resources);
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		setupSimplePager();
		//driver.initialize(this);
		setEnabledSaveTaskLimitButton(false);
	}

	/**
	 * This is used to draw the cell table for MonitoringTaskView	  
	 *
	 */
	@UiFactory
	CellTable<PersonProxy> createTable() {
		final CellTable<PersonProxy> userTable = new CellTable<PersonProxy>(PAGE_SIZE, getResources());
		userTable.setEmptyTableWidget(new Label("No Users found"));

		// user name column
		Column<PersonProxy, String> name = new Column<PersonProxy, String>(new TextCell()) {

			@Override
			public String getValue(PersonProxy userlist) {
				return userlist.getName();
			}
		};

		name.setSortable(true);
		userTable.addColumn(name,USER_NAME, "");

		// email column
		Column<PersonProxy, String> email = new Column<PersonProxy, String>(new TextCell()) {

			@Override
			public String getValue(PersonProxy userlist) {
				return userlist.getEmail();
			}
		};
		email.setSortable(true);
		userTable.addColumn(email,EMAIL, "");
		
		Column<PersonProxy, String> taskLimit = new Column<PersonProxy, String>(new TextInputCell()) {

			@Override
			public String getValue(PersonProxy userlist) {
				return "FIX ME 10";
			}
		};

		taskLimit.setSortable(true);
		userTable.addColumn(taskLimit,TASKLIMIT, "");
		
		
		
		taskLimit.setFieldUpdater(new FieldUpdater<PersonProxy, String>() {
			@Override
			public void update(int index, PersonProxy object,String value) {
				errorPanel.clear();
				logger.info("INDEX is " + index+"value="+value);
				presenter.onNewTaskLimitUpdate(object,value);
			}
		});
		
		// task created column
		Column<PersonProxy, String> taskCreated = new Column<PersonProxy, String>(new TextCell()) {

			@Override
			public String getValue(PersonProxy userlist) {	
				return "FIX ME";
			}
		};
		taskCreated.setSortable(true);
		userTable.addColumn(taskCreated, CURRENTTASK, "");

		return userTable;
	}

	/* (non-Javadoc)
	 * @see com.simple.original.client.view.MonitoringTaskLimitView#setPresenter(com.simple.original.client.view.MonitoringTaskLimitView.Presenter)
	 */
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/**
     * This method is called in MonitoringTaskLimitViewImpl for displaying of pagination
     */
    public void setupSimplePager() {
    	pager.setDisplay(userTable);
    }

	/**
     * Event fired onclick of save button
     */
	@UiHandler("save")
	void onSave(ClickEvent clickEvent) {
		presenter.onSave();
	}

	
	/* (non-Javadoc)
	 * @see com.simple.original.client.view.desktop.ViewImpl#getErrorPanel()
	 */
	@Override
	public ErrorPanel getErrorPanel() {
		return errorPanel;
	}



	/* (non-Javadoc)
	 * @see com.simple.original.client.view.desktop.ViewImpl#reset()
	 */
	@Override
	public void reset() {
		errorPanel.clear();
		setEnabledSaveTaskLimitButton(false);
	}

	@UiHandler("saveTaskLimit")
	void onSaveTaskLimit(ClickEvent clickEvent) {
		presenter.onSaveUserTaskLimit();
	}

	@UiHandler("cancel")
	void onCancel(ClickEvent clickEvent) {
		presenter.onCancel();
	}

	/* (non-Javadoc)
	 * @see com.simple.original.client.view.MonitoringTaskLimitView#getSaveButton()
	 */
	@Override
	public void setEnabledSaveButton(boolean value) {
		save.setEnabled(value);
	}

	/**
	 * Return the user CellTable
	 */
	@Ignore
	@Override
	public CellTable<PersonProxy> getUserTable() {
		return userTable;
	}

	/* (non-Javadoc)
	 * @see com.simple.original.client.view.MonitoringTaskLimitView#getDefaultTaskLimitValue()
	 */
	@Ignore
	@Override
	public String getDefaultTaskLimitValue() {
		return value.getValue();
	}

	/**
	 * Method for implementing sort functionality
	 */
	public HandlerRegistration addColumnSortHandler(ColumnSortEvent.Handler handler) {
		return userTable.addColumnSortHandler(handler);        
	}

	/**
	 * Method for getting the column index
	 */
	public int getColumnIndex(Column<PersonProxy, ?> column) {
		return this.userTable.getColumnIndex(column);
	}

	@Ignore
	@Override
	public void setEnabledSaveTaskLimitButton(boolean value) {
		saveTaskLimit.setEnabled(value);
	}

	/**
	 * Event fired onclick of searchResults button
	  */
	@UiHandler("searchResults")
	void searchResults(ClickEvent clickEvent) {
		searchQueryInput.setText(searchQueryInput.getText().trim());
		userTable.setVisibleRange(0, PAGE_SIZE);
		userTable.setVisibleRangeAndClearData(userTable.getVisibleRange(), true);
	}

	/**
	 * getter for searchQueryInput textbox
	 */
	@Ignore
	public String getSearchText() {
		return searchQueryInput.getValue();
	}

}
