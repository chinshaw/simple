package com.simple.api.domain;

/**
 * @author valva
 *
 */
public enum RecordFecthType {
	ALL_RECORDS("All"), PUBLIC_RECORDS("Public"), USER_RECORDS("User"), MY_RECORDS("My");
	
    private final String description;

	RecordFecthType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}