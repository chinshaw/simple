package com.simple.domain.export;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.simple.domain.AnalyticsOperation;
import com.simple.domain.AnalyticsOperationInput;
import com.simple.domain.AnalyticsTask;
import com.simple.domain.AnalyticsTaskExecution;
import com.simple.domain.Person;
import com.simple.domain.dashboard.Dashboard;
import com.simple.domain.metric.Metric;

/**
 * Serialization Mixin for {@link AnalyticsTask}
 * @author chinshaw
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ 
    @JsonSubTypes.Type(value = AnalyticsTask.class, name = "AnalyticsTask")})  
public interface AnalyticsTaskMixin {
    
    @JsonProperty("id")
    public abstract Long getId();
    
    @JsonProperty("isPublic")
    public abstract Boolean isPublic();
    
    @JsonProperty("operations")
    public abstract List<AnalyticsOperation> getOperations();
    
    @JsonProperty("name")
    public abstract String getName();
    
    @JsonProperty("description")
    public abstract String getDescription();
    
    @JsonProperty("owner")
    public abstract Person getOwner();
    
    @JsonIgnore
    public abstract Dashboard getDefaultDashboard();
    
    @JsonIgnore
    public AnalyticsTask deepClone();

    @JsonIgnore
    public List<AnalyticsOperationInput> getAllInputs();

    @JsonIgnore
    public List<Metric> getAllOutputs();

    @JsonIgnore
    public AnalyticsTaskExecution getLatestExecution();
    
    @JsonIgnore
    public abstract List<AnalyticsTaskExecution> getPreviousExecutions();
}
