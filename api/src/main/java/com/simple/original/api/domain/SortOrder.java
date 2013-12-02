package com.simple.original.api.domain;

public enum SortOrder {
    
    ASCENDING("ASC"),
    DESCENDING("DESC");
    
    private String jpql = "";
    SortOrder(String jpql) {
        this.jpql = jpql;
    }
    
    
    public String asJPQL() {
        return jpql;
    }
}
