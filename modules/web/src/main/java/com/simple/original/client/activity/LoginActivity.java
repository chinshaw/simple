package com.simple.original.client.activity;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.client.Application;
import com.simple.original.client.place.LoginPlace;
import com.simple.original.client.place.WelcomePlace;
import com.simple.original.client.proxy.PersonProxy;
import com.simple.original.client.view.ILoginView;

public class LoginActivity extends AbstractActivity<LoginPlace, ILoginView> implements ILoginView.Presenter {

    // private static final Logger logger = Logger.getLogger("DefaultActivity");

	@Inject
	PlaceHistoryMapper placeMapper;
	
	@Inject
	Application application;
	
	@Inject
    public LoginActivity(ILoginView view) {
        super(view);
    }

    @Override
    protected void bindToView() {
		display.getErrorPanel().clear();
		display.setPresenter(this);
    }

    @Override
    public void doAuth(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            display.showError("Unable to authenticate. Please enter valid credentials");
        } else {
        	display.getErrorPanel().clear();
            pub().authRequest().doAuthenticate(username, password).with("preferences").fire(new Receiver<PersonProxy>() {

                @Override
                public void onSuccess(PersonProxy person) {
                	display.clearFields();
                    application.setCurrentPerson(person);
                    Place place = null;
                    String defaultPlaceToken = person.getPreferences().getDefaultPlace();
                    if (defaultPlaceToken != null && !defaultPlaceToken.isEmpty()) {
                        place = placeMapper.getPlace(defaultPlaceToken);
                    } else {
                        place = new WelcomePlace();
                    }

                    placeController().goTo(place);
                }

                @Override
                public void onFailure(ServerFailure failure) {
                	display.clearFields();
                    display.showError(failure.getMessage());
                }
            });
        }
    }


	@Override
	public void doPasswordReset() {
		// TODO Auto-generated method stub
		
	}
}