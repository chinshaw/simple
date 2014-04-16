package com.simple.original.client.activity;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Inject;
import com.simple.original.client.Application;
import com.simple.original.client.place.ApplicationPlace;
import com.simple.original.client.proxy.DashboardProxy;
import com.simple.original.client.service.SearchableRequest;
import com.simple.original.client.view.IMasterLayoutPanel;
import com.simple.original.client.view.ITopPanelView;

public class TopPanelActivity extends AbstractActivity<ApplicationPlace, ITopPanelView> implements ITopPanelView.Presenter {

	@Inject
	IMasterLayoutPanel masterLayoutPanel;

	@Inject
	PlaceHistoryMapper placeHistoryMapper;
	
	@Inject
	Application app;
	
	@Inject
	public TopPanelActivity(ITopPanelView view) {
		super(view);
	}

	@Override
	protected void bindToView() {
		display.setPresenter(this);
		masterLayoutPanel.setTopPanelSize((double) 35);
		display.setFullName(app.getCurrentPerson().getName());
	}

	@Override
	public SearchableRequest<DashboardProxy> createSearchRequest() {
		return dao().createDashboardRequest();
	}

	@Override
	public void onNavigationItemSelected(String selectedPlace) {
		selectedPlace += ":"; // have to add colon for place
		Place place = placeHistoryMapper.getPlace(selectedPlace);
		if (place != null) {
			placeController().goTo(place);
		} else {
			throw new RuntimeException("Menu item " + selectedPlace + " does not have coordinating navItem");
		}
	}
}