/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.simple.original.client.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.simple.original.client.Application;
import com.simple.original.client.place.ApplicationPlace;
import com.simple.original.client.place.PlaceController;
import com.simple.original.client.proxy.PersonProxy;
import com.simple.original.client.proxy.PreferencesProxy;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.service.DaoRequestFactory;
import com.simple.original.client.service.PublicRequestFactory;
import com.simple.original.client.service.ServiceRequestFactory;
import com.simple.original.client.view.IView;

/**
 * 
 * @author chris
 */
public abstract class AbstractActivity<T extends ApplicationPlace, V extends IView> implements Activity {

	@Inject
	private EventBus eventBus;

	@Inject
	private Resources resources;

	@Inject
	private Application application;

	@Inject
	private PlaceController placeController;

	@Inject
	private DaoRequestFactory daoRequstFactory;

	@Inject
	private PublicRequestFactory publicRequestFactory;

	@Inject
	private ServiceRequestFactory serviceRequestFactory;

	protected AcceptsOneWidget parentPanel;

	private T place;

	protected final V display;

	protected final List<HandlerRegistration> handlers = new ArrayList<HandlerRegistration>();

	public AbstractActivity(V display) {
		this.display = display;
	}

	/*
	protected void setEventBus(ResettableEventBus eventBus) {
		this.eventBus = eventBus;
	}
	*/

	protected EventBus getEventBus() {
		return this.eventBus;
	}

	@Override
	public void start(AcceptsOneWidget parentPanel, com.google.gwt.event.shared.EventBus eventBus) {
		this.parentPanel = parentPanel;
		this.eventBus = eventBus;

		parentPanel.setWidget(display.asWidget());
		bindToView();
	}

	public AcceptsOneWidget getParentPanel() {
		return parentPanel;
	}

	public V getDisplay() {
		return display;
	}

	@Override
	public String mayStop() {
		return null;
	}

	@Override
	public void onCancel() {
	}

	@Override
	public void onStop() {
		cleanup();
		display.reset();
		// Dumb thing, this is a problem because the Actvity takes an older
		// shared.EventBus
		((com.google.gwt.event.shared.ResettableEventBus) eventBus).removeHandlers();
	}

	protected void addHandler(HandlerRegistration registration) {
		handlers.add(registration);
	}

	public List<HandlerRegistration> getHandlers() {
		return handlers;
	}

	protected void cleanup() {
		for (HandlerRegistration handler : handlers) {
			handler.removeHandler();
		}
	}

	/**
	 * This function will be called immediately after adding the view to the
	 * DOM. This function should be used to hook up click handlers, populate
	 * HasData handlers and such.
	 */
	protected abstract void bindToView();

	protected PlaceController placeController() {
		return placeController;
	}

	protected PersonProxy currentPerson() {
		return application.getCurrentPerson();
	}

	protected PreferencesProxy preferences() {
		return currentPerson().getPreferences();
	}

	protected T place() {
		return place;
	}

	@SuppressWarnings("unchecked")
	public void setPlace(Place place) {
		this.place = (T) place;
	}

	protected Resources resources() {
		return resources;
	}

	protected EventBus eventBus() {
		return eventBus;
	}

	protected DaoRequestFactory dao() {
		return daoRequstFactory;
	}

	protected PublicRequestFactory pub() {
		return publicRequestFactory;
	}

	protected ServiceRequestFactory service() {
		return serviceRequestFactory;
	}
}