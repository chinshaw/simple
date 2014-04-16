package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

public class AccountManagerPlace extends ApplicationPlace {

    private Long accountId;

    public AccountManagerPlace() {

    }

    public AccountManagerPlace(Long accountId) {
        this.accountId = accountId;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public static class Tokenizer implements PlaceTokenizer<AccountManagerPlace> {
        @Override
        public String getToken(AccountManagerPlace place) {
            return String.valueOf(place.getAccountId());
        }

        public AccountManagerPlace getPlace() {
            return new AccountManagerPlace();
        }

        @Override
        public AccountManagerPlace getPlace(String accountId) {
            return new AccountManagerPlace(Long.parseLong(accountId));
        }
    }

}
