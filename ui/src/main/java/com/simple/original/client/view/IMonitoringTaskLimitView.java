package com.simple.original.client.view;

import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.simple.original.client.proxy.PersonProxy;
import com.simple.original.client.view.widgets.ErrorPanel;

public interface IMonitoringTaskLimitView extends IView {

	public interface Presenter {

		/**
		 * Called when Save button is clicked
		 */
		void onSave();

		/**
		 * saves users private tasks limit in database
		 */
		void onSaveUserTaskLimit();

		void onCancel();

		/**
		 * this method is called on update of New Task Limit Value
		 */
		void onNewTaskLimitUpdate(PersonProxy object, String value);

	}

	/**
	 * Setter for Presenter class
	 * 
	 * @param presenter
	 */
	void setPresenter(Presenter presenter);

	/**
	 * getter for DefaultTasklimitValue	
	 */
	@Ignore
	public String getDefaultTaskLimitValue();

	/**
     * method to get string used for search criteria.
     * @return
     */
	@Ignore
	public String getSearchText();

	/**
	 * to enable/disable save button
	 */
	public void setEnabledSaveButton(boolean value);

	/**
	 * to enable/disable  save button for Users Private Task Limit
	 */
	public void setEnabledSaveTaskLimitButton(boolean value);

	/**
	 * CellTable for exception user list
	 * @return CellTable<PersonProxy>
	 */
	public CellTable<PersonProxy> getUserTable();

	/**
	 * getter for ErrorPanel
	 * 
	 * @return ErrorPanel
	 */
	public ErrorPanel getErrorPanel();
	
	/**
     * This method is used to add the columns to be sorted.
     * @param handler
     * @return HandlerRegistration
     */
    public HandlerRegistration addColumnSortHandler(ColumnSortEvent.Handler handler);
    
    /**
     * This method gives the column index on which sort funtionality is to be performed.
     * @param column
     * @return int column index
     */
    public int getColumnIndex(Column<PersonProxy, ?> column);

}
