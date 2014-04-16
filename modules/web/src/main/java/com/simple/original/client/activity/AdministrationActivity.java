package com.simple.original.client.activity;

import com.google.inject.Inject;
import com.simple.original.client.place.AdministrationPlace;
import com.simple.original.client.service.DaoRequestFactory;
import com.simple.original.client.service.ServiceRequestFactory;
import com.simple.original.client.view.IAdministrationView;
import com.simple.original.shared.NotificationCriticality;

public class AdministrationActivity extends
		AbstractActivity<AdministrationPlace, IAdministrationView> implements
		IAdministrationView.Presenter {

	@Inject
	DaoRequestFactory daoRequestFactory;
	
	@Inject
	ServiceRequestFactory serviceRequestFactory;
	
	@Inject
	public AdministrationActivity(IAdministrationView display) {
		super(display);
	}

	@Override
	protected void bindToView() {
		display.setPresenter(this);
	}

	@Override
	public void onSendNotification(String message) {
		serviceRequestFactory.adminiStrationServiceReqest()
				.sendNotification(NotificationCriticality.CRITICAL, message)
				.fire();
	}

    @Override
    public void clearExecutionHistory() {
    	
    }
}