package com.simple.original.client.activity;

import com.google.inject.Inject;
import com.simple.original.client.place.AnalyticsOperationsPlace;
import com.simple.original.client.place.DashboardsPlace;
import com.simple.original.client.place.DataProvidersPlace;
import com.simple.original.client.place.WelcomePlace;
import com.simple.original.client.view.IWelcomeView;

public class WelcomeActivity extends
		AbstractActivity<WelcomePlace, IWelcomeView> implements
		IWelcomeView.Presenter {

	@Inject
	public WelcomeActivity(IWelcomeView view) {
		super(view);
	}

	@Override
	protected void bindToView() {
		display.setPresenter(this);

	}

	@Override
	public void onDashboardsSelected() {
		placeController().goTo(new DashboardsPlace());
	}

	@Override
	public void onDataProvidersSelected() {
		placeController().goTo(new DataProvidersPlace());
	}

	@Override
	public void onOperationsSelected() {
		placeController().goTo(new AnalyticsOperationsPlace());

	}

	@Override
	public void onExecuteTaskSelected() {
		placeController().goTo(new DashboardsPlace());

	}

	@Override
	public void onDocumentationSelected() {

	}

	@Override
	public void onHadoopSelected() {
		placeController().goTo(new DashboardsPlace());
	}

}
