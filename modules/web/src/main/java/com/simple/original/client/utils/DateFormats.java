package com.simple.original.client.utils;

import com.google.gwt.i18n.client.DateTimeFormat;


public class DateFormats {

    /**
     * This is the date format that should be used in all tables for consistency.
     * @return
     */
    public static DateTimeFormat getTableDateFormat() {
        return DateTimeFormat.getFormat("MM/dd/yyyy HH:mm:ss");
    }
}
