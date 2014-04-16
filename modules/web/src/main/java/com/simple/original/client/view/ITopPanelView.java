package com.simple.original.client.view;

import com.simple.original.client.proxy.DashboardProxy;
import com.simple.original.client.service.SearchRequestProvider;

public interface ITopPanelView extends IView {

    public interface Presenter extends SearchRequestProvider<DashboardProxy> {

		void onNavigationItemSelected(String selectedPlace);
    }

    void setFullName(String name);

    void setPresenter(Presenter presenter);
}
