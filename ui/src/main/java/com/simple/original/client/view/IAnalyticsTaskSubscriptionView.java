package com.simple.original.client.view;

import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellList;
import com.simple.original.client.proxy.PersonProxy;
import com.simple.original.client.view.widgets.ErrorPanel;

/**
 * @author hemantha
 * 
 */
public interface IAnalyticsTaskSubscriptionView extends IView {
	public interface Presenter {

		/**
		 * Calls this method implementation when Save button is clicked
		 */
		void onSave();

		/**
		 * Calls this method implementation when Cancel button is clicked
		 */
		void onCancel();

		/**
		 * Calls this method implementation when Add button of the Virtual Factory users list is clicked
		 */
		void onAddVirtualFactoryUsers();

		/**
		 * Calls this method implementation when Remove button is clicked
		 */
		void onRemoveUsers();

	}

	/**
	 * Setter for Presenter class
	 * @param presenter
	 */
	public void setPresenter(Presenter presenter);

	/**
	 * Cell list of Virtual factory users
	 * @return CellList<PersonProxy>
	 */
	public CellList<PersonProxy> getUsersList();

 	/**
     * get errorPanel
	 * @return ErrorPanel
	 */
	public ErrorPanel getErrorPanel();


	/**
	 * Used to get the subscribers celllist
	 * @return AbstractHasData<PersonProxy>
	 */
	AbstractHasData<PersonProxy> getSubsribersList();

	/**
	 * ListEditor for subscribers celllist
	 * @return ListEditor<PersonProxy, ?>
	 */
	@Ignore
	ListEditor<PersonProxy, ?> getSubscribersEditor();

	void setEnabledSaveButton(boolean value);

	void setEnabledCancelButton(boolean value);

}
