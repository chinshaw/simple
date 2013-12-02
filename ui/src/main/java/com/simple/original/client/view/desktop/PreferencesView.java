package com.simple.original.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.simple.original.client.proxy.ApplicationBookmarkProxy;
import com.simple.original.client.proxy.PreferencesProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IPreferencesView;
import com.simple.original.client.view.widgets.BookmarkCellRenderer;
import com.simple.original.client.view.widgets.BookmarkCellRenderer.Handler;
import com.simple.original.client.view.widgets.ErrorPanel;

public class PreferencesView extends AbstractView implements IPreferencesView,
		Editor<PreferencesProxy>, Handler {

	/**
	 * This is my user editor driver.
	 * 
	 * @author chinshaw
	 */

	interface PreferencesReqFactoryEditorDriver extends
			RequestFactoryEditorDriver<PreferencesProxy, PreferencesView> {

	}

	private final PreferencesReqFactoryEditorDriver driver = GWT
			.create(PreferencesReqFactoryEditorDriver.class);

	/**
	 * UiBinder implementation.
	 * 
	 * @author chinshaw
	 * 
	 */
	@UiTemplate("PreferencesView.ui.xml")
	interface Binder extends UiBinder<Widget, PreferencesView> {
	}

	/**
	 * Save Button
	 */
	@UiField
	Button save;

	/**
	 * Cancel Button
	 */
	@UiField
	Button cancel;

	/**
	 * This holds the mailid of the user taken from LDAP
	 */
	@UiField
	TextBox subscriberMailId;

	/**
	 * This holds the Mobile Number of the user grabbed from LDAP system
	 */
	@UiField
	TextBox subscriberCellNumber;

	/**
	 * Flag representing the type of notification. If this is checked the user can be subscribed to his Emailid
	 */
	@UiField
	CheckBox emailFlag;

	/**
	 * This represents the type of alert notification for current logged in user. If this is checked, the user can be subscribed to an alert through his Mobile
	 */
	@UiField
	CheckBox smsFlag;
	
	@Editor.Ignore
	@UiField(provided = true)
	CellList<ApplicationBookmarkProxy> favorites;
	

	/**
	 * ValueListBox to show all the analytics tasks
	 */
	@Path("cellPhoneProvider")
	@UiField(provided = true)
	ValueListBox<String> cellPhoneProviderList = new ValueListBox<String>(
			new AbstractRenderer<String>() {
				@Override
				public String render(String provider) {
					return provider == null ? "Select a Provider" : provider;
				}
			});	

	/**
	 * ErrorPanel to show the errors if any
	 */
	@Ignore
	@UiField
	ErrorPanel errorPanel;

	/**
	 * Presenter
	 */
	private Presenter presenter = null;

	/**
	 * Required default constructor
	 * @param resources
	 */
	@Inject
	public PreferencesView(EventBus eventBus, Resources resources) {
		super(resources);

		
		BookmarkCellRenderer.Renderer bookmarkRenderer = GWT.create(BookmarkCellRenderer.Renderer.class);
		BookmarkCellRenderer renderer = new BookmarkCellRenderer(bookmarkRenderer, this);
		favorites = new CellList<ApplicationBookmarkProxy>(renderer);
		
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		driver.initialize(this);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public ErrorPanel getErrorPanel() {
		return errorPanel;
	}

	@UiHandler("save")
	void onSavePreferences(ClickEvent clickEvent) {
		errorPanel.clear();
		presenter.onSavePreferences();
	}

	@UiHandler("cancel")
	void onCancel(ClickEvent clickEvent) {
		errorPanel.clear();
		presenter.onCancel();
	}

	@Override
	public RequestFactoryEditorDriver<PreferencesProxy, ?> getFactEditorDriver() {
		return driver;
	}

	@Ignore
	@Override
	public ValueListBox<String> getCellPhoneProviderList() {
		return cellPhoneProviderList;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Ignore
	public CheckBox getSmsFlagCheckBox() {
		return smsFlag;
	}

	@Override
	@Ignore
	public TextBox getCellNumberBox() {
		return subscriberCellNumber;
	}

	@Override
	@Ignore
	public TextBox getEmailTextBox() {
		return subscriberMailId;
	}

	@Override
	public Button getSaveButton() {
		return save;
	}

	@Override
	public HasData<ApplicationBookmarkProxy> getFavoritesDisplay() {
		return favorites;
	}

	@Override
	public void onDeleteBookmark(ApplicationBookmarkProxy bookmark) {
		if (presenter == null) {
			throw new RuntimeException("presenter is null, you won't be able to delete bookmarks");
		}
		presenter.onDeleteBookmark(bookmark);
		
	}
}