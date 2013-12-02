package com.simple.original.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.client.place.AnalyticsTaskExecPlace;
import com.simple.original.client.place.LoginPlace;
import com.simple.original.client.place.PlaceController;
import com.simple.original.client.proxy.PersonProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.service.DaoRequestFactory;
import com.simple.original.client.service.RemoteDebugService;
import com.simple.original.client.utils.ClientUtils;
import com.simple.original.client.view.IMasterLayoutPanel;

public class Simple implements EntryPoint {

	private final InjectorProvider provider = GWT.create(InjectorProvider.class);

	private final IOCBaseInjector injector = provider.get();

	@Inject
	private IMasterLayoutPanel masterLayoutPanel;

	@Inject
	DaoRequestFactory daoRequestFactory;

	@Inject
	Resources resources;

	@Inject
	PlaceController placeController;

	@Inject
	Application application;

	private Place defaultPlace = new AnalyticsTaskExecPlace();

	@Override
	public void onModuleLoad() {
		injector.injectMembers(this);

		if (Window.Location.getParameterMap().containsKey("debug")) {
			Window.alert("Remote Debugging will be enabled, see server log for debug information");
			RemoteDebugService.enable();
		}

		RootLayoutPanel.get().add(masterLayoutPanel);
		StyleInjector.inject(resources.style().getText());

		PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(injector.placeHistoryMapper());

		// Start PlaceHistoryHandler with our PlaceHistoryMapper
		historyHandler.register(placeController, injector.eventBus(), defaultPlace);

		configureActivities();
		startApplication(historyHandler);

	}

	private void startApplication(final PlaceHistoryHandler historyHandler) {

		if (ClientUtils.hasValidSessionId()) {
			daoRequestFactory.personRequest().getCurrentPerson().with(PersonProxy.AUTH_PROPERTIES).fire(new Receiver<PersonProxy>() {

				@Override
				public void onSuccess(PersonProxy person) {
					if (person != null) {
						application.setCurrentPerson(person);
						historyHandler.handleCurrentHistory();

					} else {
						placeController.goTo(new AnalyticsTaskExecPlace());
					}
				}

				public void onFailure(ServerFailure error) {
					// Lets just assume that the user is not authenticated and
					// we need to send them to the login screen.
					placeController.goTo(new LoginPlace());
				}
			});
		} else {
			placeController.goTo(new LoginPlace());
		}
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
}
