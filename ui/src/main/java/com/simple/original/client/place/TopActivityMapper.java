package com.simple.original.client.place;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.simple.original.client.Application;
import com.simple.original.client.IOCBaseInjector;
import com.simple.original.client.IOCDesktopInjector;

public class TopActivityMapper implements ActivityMapper {

	@Inject
	Application application;

	
	IOCBaseInjector injector; 
    
	@Inject
    public TopActivityMapper(IOCDesktopInjector injector) {
    	this.injector = injector;
    }

    @Override
    public Activity getActivity(Place place) {

        if (application.getCurrentPerson() == null) {
            return null;
        }

        if (place instanceof ApplicationPlace) {
        	return injector.topPanelActivity();
        }

        return null;

    }
}
