package com.simple.original.client.view;

import com.google.gwt.user.client.ui.AcceptsOneWidget;

public interface INavigationView extends IView {

    public interface Presenter {
        /**
         * This is the string name for the place to go to
         * the place mapper will look it up so it should match the class name
         * in PlaceHistoryMapper.java
         * @param place
         */
        void onNavigationItemSelected(String place);
    }
    
    
    void setPresenter(Presenter presenter);

    void setCritalAlertCount(Integer count);

    void setMajorAlertCount(Integer count);

    void setMinorAlertCount(Integer count);

    AcceptsOneWidget getGlobalStatusContainer();

	void enableAdmin();
	
	void disableAdmin();

}
