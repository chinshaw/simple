package com.simple.domain;

/**
 * Simple wrapper class for speed to return just a list of operation names.
 * 
 * @author chinshaw
 */

public class AnalyticsOperationName {

    private Long id;

    private String name;

    public AnalyticsOperationName() {
    }

    public AnalyticsOperationName(Long id, String name) {
        setId(id);
        setName(name);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
