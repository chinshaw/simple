package com.simple.original.client.view;

import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.view.client.HasData;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.simple.original.client.proxy.ApplicationBookmarkProxy;
import com.simple.original.client.proxy.PreferencesProxy;
import com.simple.original.client.view.widgets.ErrorPanel;


public interface IPreferencesView extends IView {

    public interface Presenter extends com.simple.original.client.view.widgets.BookmarkCellRenderer.Handler {

        /**
         * Called when save button is clicked
         */
        void onSavePreferences();
        
        /**
         * Called when Cancle button is clicked
         */
        void onCancel();
        
    }
    
    /**
     * Sets the presenter object this interface
     * @param presenter
     */
    public void setPresenter(Presenter presenter);

    /**
     * Get the driver used to edit preferences in the view.
     */

     RequestFactoryEditorDriver<PreferencesProxy, ?> getFactEditorDriver();
     
     /**
      * ValueListBox holds all the Cell phone providers read from CellPhoneProviders.xml file
     * @return
     */
    @Ignore
     ValueListBox<String> getCellPhoneProviderList();

	/**
	 * Error Panel to show the errors for user actions
	 * @return
	 */
	public  ErrorPanel getErrorPanel();
	
	/**
	 * CheckBox for SmsFlag
	 * @return
	 */
    @Ignore
	public CheckBox getSmsFlagCheckBox();
	
/**
	 * TextBox for subscriber's email id
	 * @return
	 */ 
    @Ignore
	public TextBox getEmailTextBox();

	/**
	 * TextBox for subscriber's mobile number
	 * @return
	 */
    @Ignore
	public TextBox getCellNumberBox();
    
    /**
     * 
     * button for save 
     * @return
     */
    public Button getSaveButton();

	public HasData<ApplicationBookmarkProxy> getFavoritesDisplay();
}
