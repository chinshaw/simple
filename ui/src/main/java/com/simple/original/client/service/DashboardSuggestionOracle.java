package com.simple.original.client.service;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.api.domain.RecordFecthType;
import com.simple.original.api.domain.SortOrder;
import com.simple.original.client.proxy.DashboardProxy;

public class DashboardSuggestionOracle extends SuggestOracle {

    static boolean isSearching = false;

    public List<DashboardSuggestion> currentSuggestions = new ArrayList<DashboardSuggestion>();

    public class DashboardSuggestion implements Suggestion {

        private DashboardProxy dashboard;

        public DashboardSuggestion(DashboardProxy dashboard) {
            this.dashboard = dashboard;
        }

        @Override
        public String getDisplayString() {
            return dashboard.getName();
        }

        @Override
        public String getReplacementString() {
            return getDisplayString();
        }

        public DashboardProxy getDashboard() {
            return dashboard;
        }
    }

    private SearchRequestProvider<DashboardProxy> provider;

    public DashboardSuggestionOracle() {
    }

    public DashboardSuggestionOracle(SearchRequestProvider<DashboardProxy> provider) {
        setSearchProvider(provider);
    }

    public void setSearchProvider(SearchRequestProvider<DashboardProxy> provider) {
        this.provider = provider;
    }

    @Override
    public void requestSuggestions(final Request request, final Callback callback) {
        if (!isSearching && provider != null) {
            isSearching = true;
            
            provider.createSearchRequest().search(0, request.getLimit(), RecordFecthType.ALL_RECORDS, request.getQuery(), "name", "name", SortOrder.DESCENDING).fire(
                    new Receiver<List<DashboardProxy>>() {

                        @Override
                        public void onSuccess(List<DashboardProxy> dashboards) {
                            isSearching = false;
                            Response response = new Response();
                            currentSuggestions.clear();
                            for (DashboardProxy dashboard : dashboards) {
                                currentSuggestions.add(new DashboardSuggestion(dashboard));
                            }

                            response.setSuggestions(currentSuggestions);
                            callback.onSuggestionsReady(request, response);
                        }

                        public void onFailure(ServerFailure failure) {
                            isSearching = false;
                            currentSuggestions.clear();
                        }
                    });
        }
    }

    public List<DashboardSuggestion> getCurrentSuggestions() {
        return currentSuggestions;
    }

    public DashboardSuggestion getSuggestionByName(String name) {
        if (currentSuggestions == null | currentSuggestions.isEmpty()) {
            return null;
        }

        for (DashboardSuggestion suggestion : currentSuggestions) {
            if (suggestion.getReplacementString().equals(name)) {
                return suggestion;
            }
        }
        return null;
    }
}
