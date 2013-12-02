package com.simple.domain.export;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.simple.domain.AnalyticsOperation;
import com.simple.domain.AnalyticsOperationInput;
import com.simple.domain.JavaAnalyticsOperation;
import com.simple.domain.RAnalyticsOperation;
import com.simple.domain.metric.Metric;

/**
 * Jackson mixin annotations for {@link AnalyticsOperation}
 * 
 * @author chinshaw 
 * 
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ 
    @JsonSubTypes.Type(value = AnalyticsOperation.class, name = "AnalyticsOperation"),
    @JsonSubTypes.Type(value = RAnalyticsOperation.class, name = "RAnalyticsOperation"),
    @JsonSubTypes.Type(value = JavaAnalyticsOperation.class, name = "JavaAnalyticsOperation"),})
public abstract class AnalyticsOperationMixin {

    @JsonProperty
    public abstract List<AnalyticsOperationInput> getInputs();

    @JsonProperty
    public abstract List<Metric> getOuputs();
}