package com.simple.original.api.analytics;

import java.util.List;

public interface IComplexInput extends IAnalyticsOperationInput {

    public abstract List<? extends IAnalyticsOperationInput> getInputs();

    public abstract String getValueAsString();

}