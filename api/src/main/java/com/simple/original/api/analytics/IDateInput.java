package com.simple.original.api.analytics;

import java.util.Date;

public interface IDateInput extends IAnalyticsOperationInput {

    public abstract Date getValue();

    public abstract String getValueAsString();

}