package com.simple.original.client.activity;

import com.google.inject.Inject;
import com.simple.original.client.place.WelcomePlace;
import com.simple.original.client.view.IWelcomeView;

public class WelcomeActivity extends AbstractActivity<WelcomePlace, IWelcomeView> {

	@Inject
	public WelcomeActivity(IWelcomeView view) {
		super(view);

	}

	@Override
	protected void bindToView() {
		// TODO Auto-generated method stub

	}

}
