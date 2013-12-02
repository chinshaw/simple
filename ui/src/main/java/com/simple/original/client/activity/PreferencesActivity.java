package com.simple.original.client.activity;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.client.events.BookmarkCreatedEvent;
import com.simple.original.client.events.BookmarkDeletedEvent;
import com.simple.original.client.events.NotificationEvent;
import com.simple.original.client.place.PreferencesPlace;
import com.simple.original.client.proxy.ApplicationBookmarkProxy;
import com.simple.original.client.proxy.PreferencesProxy;
import com.simple.original.client.service.DaoRequestFactory.PersonRequest;
import com.simple.original.client.view.IPreferencesView;
import com.simple.original.shared.NotificationCriticality;

/**
 * For the current person logged in this will find current preferences and shows
 * the preferences on screen and allow user to edit the current preferences and
 * save
 */
public class PreferencesActivity extends AbstractActivity<PreferencesPlace, IPreferencesView> implements IPreferencesView.Presenter, BookmarkCreatedEvent.Handler {

	private static final Logger logger = Logger.getLogger("PreferencesActivity");

	@Inject
	public PreferencesActivity(IPreferencesView view) {
		super(view);
	}
	
	private ListDataProvider<ApplicationBookmarkProxy> favorites = new ListDataProvider<ApplicationBookmarkProxy>();

	/**
	 * For the current person logged in this will find current preferences and
	 * shows the preferences on screen and allow user to edit the current
	 * preferences and save
	 */
	@Override
	protected void bindToView() {
		display.setPresenter(this);
		display.getErrorPanel().clear();
		getCellPhoneProviders();
		getPreferences();
	}

	/**
	 * fetch Current Person with preferences details from datatbase
	 */
	private void getPreferences() {
		dao().personRequest().getPreferencesForCurrentUser().with(PreferencesProxy.BOOKMARK_PROPERTIES).fire(new Receiver<PreferencesProxy>() {

			@Override
			public void onSuccess(PreferencesProxy preferences) {
				editPreferences(preferences);
			}
		});
	}

	/**
	 * Get the list of CellPhone providers and set them to CellPhone providers
	 * list box
	 */
	private void getCellPhoneProviders() {
		logger.info("Setting current preferences in UI");
	}

	/**
	 * This will fire the editor off
	 */
	protected void editPreferences(PreferencesProxy preferences) {

		PersonRequest request = dao().personRequest();
		RequestFactoryEditorDriver<PreferencesProxy, ?> driver = display.getFactEditorDriver();
		request.savePreferences(preferences).with(driver.getPaths());
		driver.edit(preferences, request);
		
		display.getSaveButton().setEnabled(true);
		
		favorites.addDataDisplay(display.getFavoritesDisplay());
		List<ApplicationBookmarkProxy> bookmarks = preferences.getBookmarks();
		
		if (bookmarks != null) {
			favorites.getList().addAll(bookmarks);	
		}
	}

	/**
	 * Called on click of save button
	 */
	@Override
	public void onSavePreferences() {

		logger.info("Inside onSavePreferences()");

		if (validateFields()) {
			display.getSaveButton().setEnabled(false);
			RequestFactoryEditorDriver<PreferencesProxy, ?> driver = display.getFactEditorDriver();
			PersonRequest request = (PersonRequest) driver.flush();
			if (driver.hasErrors()) {
				List<EditorError> errors = driver.getErrors();
				for (EditorError error : errors) {
					logger.info("Errors occurred in Preferences editor" + error.getMessage());
				}
				return;
			}

			request.fire(new Receiver<Void>() {

				public void onFailure(ServerFailure error) {
					display.showError(error.getMessage());
					logger.info("error occured" + error.getMessage());
					super.onFailure(error);
					display.getSaveButton().setEnabled(true);
				}

				@Override
				public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
					for (ConstraintViolation<?> voilation : violations) {
						if (voilation.getPropertyPath().toString().trim().equalsIgnoreCase("subscriberCellNumber")) {
							logger.info(voilation.getMessage());
							display.showError("Mobile Phone Number is not valid. Please enter mobile number using the following format xxx-xxx-xxxx where x is a number");
						}
						if (voilation.getPropertyPath().toString().trim().equalsIgnoreCase("subscriberMailId")) {
							display.showError("Email Address field cannot be blank or is not valid. Please enter valid email address");
						}
					}
					display.getSaveButton().setEnabled(true);
				}

				@Override
				public void onSuccess(Void response) {
					getPreferences();
					logger.info("Preferences Saved Successfully");
					display.getErrorPanel().clear();
					// Global Status Window
					NotificationEvent ne = new NotificationEvent("Saved Successfully");
					eventBus().fireEvent(ne);
				}
			});
		}
	}

	/**
	 * relationship validation of UI fields based on requirement
	 */
	private boolean validateFields() {
		// Need to be replaced with Server side validation.
		if (display.getEmailTextBox().getText() == null || display.getEmailTextBox().getText().trim().equals("")) {
			display.showError("Please enter a email id ");
			return false;
		}
		// This is relationship validation between fields of Preferences object
		if (display.getSmsFlagCheckBox().getValue()) {
			if (display.getCellNumberBox().getText() == null || display.getCellNumberBox().getText().trim().equals("")) {
				display.showError("Please enter a valid mobile phone number");
				return false;
			}
			if (display.getCellNumberBox().getText() != null && display.getCellNumberBox().getText().length() > 0) {
				if (display.getCellPhoneProviderList().getValue() != null && display.getCellPhoneProviderList().getValue().equals("None")) {
					display.showError("Please select a Mobile Phone Provider from the list provided");
					return false;
				}
			}
		} else {
			if (display.getCellNumberBox().getText() != null && display.getCellNumberBox().getText().length() > 0) {
				if (display.getCellPhoneProviderList().getValue() == null || display.getCellPhoneProviderList().getValue().equals("None")) {
					display.showError("Please select a Mobile Phone Provider from the list provided");
					return false;
				}
			}
			if (display.getCellPhoneProviderList().getValue() != null && !display.getCellPhoneProviderList().getValue().equals("None")) {
				if (display.getCellNumberBox().getText() == null || display.getCellNumberBox().getText().trim().equals("")) {
					display.showError("Enter Cell Phone number as you have selected Cell Phone Provider");
					return false;
				}
			}

		}

		return true;
	}
	
	public void onDeleteBookmark() {
		
	}
	

	@Override
	public void onCancel() {
		getPreferences();
	}

	@Override
	public void onDeleteBookmark(final ApplicationBookmarkProxy bookmark) {
		dao().personRequest().removeBookmark(bookmark).fire(new Receiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				if (response) {
					favorites.getList().remove(bookmark);
					eventBus().fireEvent(new BookmarkDeletedEvent(bookmark));
				}
			}
			
			public void onFailure(ServerFailure failure) {
				eventBus().fireEvent(new NotificationEvent(NotificationCriticality.CRITICAL, "Unable to save your bookmark, see logs for details"));
			}			
		});
	}

	@Override
	public void onBookmarkCreated(BookmarkCreatedEvent event) {
		favorites.getList().add(event.getBookmark());
	}
}
