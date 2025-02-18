package com.simple.domain.model;

/**
 * Class that is useful for retrieving just the names of the tasks.
 * 
 * @author chinshaw
 */
public class AnalyticsTaskName {

    private Long id;

    private String name;

    public AnalyticsTaskName() {
    }

    public AnalyticsTaskName(Long id, String name) {
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
