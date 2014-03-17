package com.simple.original.client.place;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.simple.original.client.Application;
import com.simple.original.client.IOCDesktopInjector;
import com.simple.original.client.activity.AbstractActivity;

public class ContentActivityMapper implements ActivityMapper {

	private static final Logger logger = Logger.getLogger(ContentActivityMapper.class.getName());

	private Map<Class<? extends Place>, AbstractActivity<?, ?>> activityMap = new HashMap<Class<? extends Place>, AbstractActivity<?, ?>>();

	@Inject Application application;
	
	@Inject
	public ContentActivityMapper(IOCDesktopInjector injector) {
		addProvider(AnalyticsOperationsPlace.class, injector.analyticsOperationsActivity());
		addProvider(AnalyticsTasksPlace.class, injector.analyticsTasksActivity());
		addProvider(AnalyticsTaskExecPlace.class, injector.analyticsTaskExecActivity());

		addProvider(AnalyticsOperationPlace.class, injector.analyticsOperationBuilderActivity());
		addProvider(DashboardPlace.class, injector.dashboardActivity());
		addProvider(DashboardsPlace.class, injector.dashboardsActivity());
		addProvider(DataProvidersPlace.class, injector.dataProvidersActivity());
		addProvider(DashboardDesignerPlace.class, injector.dashboardDesignerActivity());
		addProvider(OrchestratorPlace.class, injector.hbaseActivity());
		addProvider(LoginPlace.class, injector.loginActivity());
		addProvider(WelcomePlace.class, injector.welcomeActivity());
	}

	public void addProvider(Class<? extends Place> placeClass, AbstractActivity<?, ?> activity) {
		activityMap.put(placeClass, activity);
	}

	@Override
	public Activity getActivity(Place place) {
		logger.fine("Place is " + place);
		AbstractActivity<?, ?> activity = null;
		
		
		logger.info("Application user is " + application.getCurrentPerson());
		if (application.getCurrentPerson() == null) {
			activity =  activityMap.get(LoginPlace.class);
		} else {
			activity = activityMap.get(place.getClass());
		}

		if (activity != null) {
			activity.setPlace(place);
			return activity;
		}
		
		throw new IllegalArgumentException("Invalid place " + place);
	}
}
