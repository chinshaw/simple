package com.simple.original.client.activity;

import java.util.List;

import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.simple.original.client.place.DashboardDesignerPlace;
import com.simple.original.client.place.DashboardsPlace;
import com.simple.original.client.proxy.DashboardProxy;
import com.simple.original.client.view.IDashboardsView;

public class DashboardsActivity extends AbstractActivity<DashboardsPlace, IDashboardsView> implements IDashboardsView.Presenter {

	final Receiver<List<DashboardProxy>> dashboardsReceiver = new Receiver<List<DashboardProxy>>() {

		@Override
		public void onSuccess(List<DashboardProxy> response) {
			display.getDashboardDataProvider().setRowData(0, response);
		}
	};
	
	@Inject
	public DashboardsActivity(IDashboardsView view) {
		super(view);
	}

	@Override
	protected void bindToView() {
		display.setPresenter(this);
		dao().createDashboardRequest().listAll().to(dashboardsReceiver).fire();
	}

	@Override
	public void onNewDashboard() {
		placeController().goTo(new DashboardDesignerPlace(null));
	}
}
