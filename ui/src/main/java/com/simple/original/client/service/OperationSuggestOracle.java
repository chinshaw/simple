package com.simple.original.client.service;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.api.domain.RecordFecthType;
import com.simple.original.api.domain.SortOrder;
import com.simple.original.client.proxy.AnalyticsOperationProxy;

public class OperationSuggestOracle extends SuggestOracle {

    static boolean isSearching = false;

    public List<OperationSuggestion> currentSuggestions = new ArrayList<OperationSuggestion>();

    public class OperationSuggestion implements Suggestion {

        private AnalyticsOperationProxy operation;

        public OperationSuggestion(AnalyticsOperationProxy operation) {
            this.operation = operation;
        }

        @Override
        public String getDisplayString() {
            return operation.getName();
        }

        @Override
        public String getReplacementString() {
            return getDisplayString();
        }

        public AnalyticsOperationProxy getOperation() {
            return operation;
        }
    }

    private SearchRequestProvider<AnalyticsOperationProxy> provider;

    public OperationSuggestOracle() {
    }

    public OperationSuggestOracle(SearchRequestProvider<AnalyticsOperationProxy> provider) {
        setSearchProvider(provider);
    }

    public void setSearchProvider(SearchRequestProvider<AnalyticsOperationProxy> provider) {
        this.provider = provider;
    }

    @Override
    public void requestSuggestions(final Request request, final Callback callback) {
        if (!isSearching && provider != null) {
            isSearching = true;
            
            provider.createSearchRequest().search(0, request.getLimit(), RecordFecthType.ALL_RECORDS, request.getQuery(), "name", "name", SortOrder.DESCENDING).fire(
                    new Receiver<List<AnalyticsOperationProxy>>() {

                        @Override
                        public void onSuccess(List<AnalyticsOperationProxy> operations) {
                            isSearching = false;
                            Response response = new Response();
                            currentSuggestions.clear();
                            for (AnalyticsOperationProxy operation : operations) {
                                currentSuggestions.add(new OperationSuggestion(operation));
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

    public List<OperationSuggestion> getCurrentSuggestions() {
        return currentSuggestions;
    }

    public OperationSuggestion getSuggestionByName(String name) {
        if (currentSuggestions == null | currentSuggestions.isEmpty()) {
            return null;
        }

        for (OperationSuggestion suggestion : currentSuggestions) {
            if (suggestion.getReplacementString().equals(name)) {
                return suggestion;
            }
        }
        return null;
    }
}
