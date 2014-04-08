package com.simple.original.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.client.place.LoginPlace;
import com.simple.original.client.place.PlaceController;
import com.simple.original.client.place.WelcomePlace;
import com.simple.original.client.proxy.PersonProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.service.PublicRequestFactory;
import com.simple.original.client.service.RemoteDebugService;
import com.simple.original.client.view.IMasterLayoutPanel;

public class Simple implements EntryPoint {

	private static final Logger logger = Logger.getLogger(Simple.class.getName());
	
	private final InjectorProvider provider = GWT.create(InjectorProvider.class);

	private final IOCBaseInjector injector = provider.get();

	@Inject
	private IMasterLayoutPanel masterLayoutPanel;

	@Inject
	PublicRequestFactory publicRequestFactory;

	@Inject
	Resources resources;

	@Inject
	PlaceController placeController;

	/**
	 * Client side application singleton for storing global variables.
	 */
	@Inject
	Application app;

	private Place defaultPlace = new WelcomePlace();

	@Override
	public void onModuleLoad() {
		injector.injectMembers(this);

		if (Window.Location.getParameterMap().containsKey("debug")) {
			Window.alert("Remote Debugging will be enabled, see server log for debug information");
			RemoteDebugService.enable();
		}

		RootLayoutPanel.get().add(masterLayoutPanel);
		
		resources.style().ensureInjected();
		resources.cellListStyle().ensureInjected();
		
		PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(injector.placeHistoryMapper());

		// Start PlaceHistoryHandler with our PlaceHistoryMapper
		historyHandler.register(placeController, injector.eventBus(), defaultPlace);

		configureActivities();
		startApplication(historyHandler);

	}


	private void configureActivities() {
		ActivityManager contentMapper = new ActivityManager(injector.contentActivityMapper(), injector.eventBus());
		contentMapper.setDisplay(injector.getMasterLayoutPanel().getCenterDisplay());

		ActivityManager topMapper = new ActivityManager(injector.topActivityMapper(), injector.eventBus());
		topMapper.setDisplay(injector.getMasterLayoutPanel().getTopDisplay());
	}

	protected void initLogger() {
		// Configure logger functionality.

		if (GWT.isProdMode()) {
			final Logger logger = Logger.getLogger("");
			logger.addHandler(injector.getLoggerPanel());
			GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

				@Override
				public void onUncaughtException(Throwable e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
			});
		}
	}
	
	/**
	 * This will call the server and try to fetch the current person if they are logged in.
	 * If they are logged in and a person is found for an existing session then the user is
	 * configured for the application as a whole.
	 * @param historyHandler
	 */
	private void startApplication(final PlaceHistoryHandler historyHandler) {
		publicRequestFactory.authRequest().getCurrentPerson().with(PersonProxy.AUTH_PROPERTIES).fire(new Receiver<PersonProxy>() {

			@Override
			public void onSuccess(PersonProxy person) {
				app.setCurrentPerson(person);
				historyHandler.handleCurrentHistory();
			}

			public void onFailure(ServerFailure error) {
				// Lets just assume that the user is not authenticated and
				// we need to send them to the login screen.
				
				GWT.log("Got server failure " + error.getMessage());
				placeController.goTo(new LoginPlace());
			}
		});
	}
}
