package com.simple.original.client.view;


import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.simple.original.client.proxy.AnalyticsOperationOutputProxy;
import com.simple.original.client.proxy.AnalyticsTaskMonitorProxy;
import com.simple.original.client.proxy.AnalyticsTaskProxy;
import com.simple.original.client.proxy.PersonProxy;
import com.simple.original.client.view.widgets.ErrorPanel;


/**
 * @author nallaraj
 * @description View class for AlertDefinitin Modify/Create screen
 */
public interface IAlertDefinitionEditView extends IView, Editor<AnalyticsTaskMonitorProxy> {
	
	 interface Presenter {
		    
		    /**
		     * Called on selecting any of the Analytics task
		     * @param task
		     */
		    void onAnalyticsTaskSelect(AnalyticsTaskProxy task);
		    
			/**
			 * Called when Save button is clicked
			 */
			void onSave();
			
			/**
			 * Called when Remove button is clicked
			 */
			void onRemoveUsers();
			
			/**
			 * Called when Add button of the users list is clicked
			 */
			void onAddUnsubscribedUsers();

			/**
			 * Called when Cancel button is clicked
			 */
			void onCancel();
			
			/**
			 * Called on select of Public Access check box
			 */
			void onSelectOfPublicAcessCheckBox(boolean isPublicValue);
			
	 }
	 
	/**
	 * Setter for Presenter class
	 * @param presenter
	 */
	void setPresenter(Presenter presenter);
	
	/**
	 * Cell list of Virtual factory users
	 * @return CellList<PersonProxy>
	 */
	public CellList<String> getUnsubscribedUserList();
	
	/**
	 * Used to set the description of the selected Analytics Task in TaskDescription TextArea 
	 * @param description
	 */
	void setTaskDescription(String description);
	
	/**
	 * Get the RequestFactoryEditorDriver
	 * @return RequestFactoryEditorDriver<AlertDefinitionProxy, ?>
	 */
	RequestFactoryEditorDriver<AnalyticsTaskMonitorProxy, ?> getEditorDriver();

	/**
	 * Used to set the acceptable analytics tasks in Analytics tasks ValueList box
	 * @param tasks
	 */
	void setAcceptableTasks(List<AnalyticsTaskProxy> tasks);

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

	/**
	 * Value listbox of analytics tasks
	 * @return ValueListBox<AnalyticsTaskProxy>
	 */
	@Ignore
	public ValueListBox<AnalyticsTaskProxy> getAnalyticsListBox();

	/**
	 * Returns the valuelistbox used to hold the Metrics(outputs) of the selected analytics task
	 * @return
	 */
	@Ignore
	public ValueListBox<AnalyticsOperationOutputProxy> getTaskMetricsListBox();
	
	/**
	 * TextArea holding task description of selected analytics task
	 * @return
	 */
	@Ignore
	TextArea getTaskDescription();

	/**
	 * Used to set the acceptable outputs for Task Metrics valuelistbox
	 * @param metrics
	 */
	void setAcceptableOutputs(List<AnalyticsOperationOutputProxy> outputs);

	/**
	 * Returns Save Button
	 * @return Button
	 */
	public Button getSaveButton();

 	/**
     * get errorPanel
	 * @return ErrorPanel
	 * 
	 */
	public ErrorPanel getErrorPanel();
	
	/**
	 * Text box for Alert Name
	 * @return
	 */
	@Ignore
	public TextBox getAlertNameBox();

	/**
	 * For setting isPublic checkbox value 
	 * @param value
	 */
	void setEnabledPublic(Boolean value);

	/**
	 * Sets the visibility of alertSubscriptionPanel in UI 
	 * @param value
	 */
	void setVisibleAlertSubscriptionPanel(Boolean value);
	
	
	/**
	 * Gets the value of isPublic checkbox
	 * @param value
	 */
	@Ignore
	boolean getIsPublicValue();

	void setEnabledSaveButton(Boolean value);

	void setEnabledCancelButton(Boolean value);
}
