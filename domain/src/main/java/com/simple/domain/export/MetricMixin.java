package com.simple.domain.export;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.simple.domain.model.metric.Metric;
import com.simple.domain.model.metric.MetricCollection;
import com.simple.domain.model.metric.MetricNumber;
import com.simple.domain.model.metric.MetricPlot;
import com.simple.domain.model.metric.MetricString;


/**
 * Mixin class for {@link Metric} class
 * @author chinshaw
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ 
    @JsonSubTypes.Type(value = MetricCollection.class, name = "MetricCollection"),
    //@JsonSubTypes.Type(value = MetricDataFrame.class, name = "MetricDataFrame"),
    @JsonSubTypes.Type(value = MetricNumber.class, name = "MetricNumber"),
    @JsonSubTypes.Type(value = MetricPlot.class, name = "MetricStaticChart"),
    @JsonSubTypes.Type(value = MetricString.class, name = "MetricString"),
    //@JsonSubTypes.Type(value = MetricVectorNumber.class, name = "MetricVectorNumber")
    })
public interface MetricMixin {
    
    @JsonIgnore
    public String toString();
    
    @JsonIgnore
    public boolean hasMetricViolations();
    
    @JsonIgnore
    public Metric getParent();

    @JsonIgnore
    public boolean isCloned();

    @JsonIgnore
    public Metric clone();
    
    @JsonIgnore
    public Metric cloneDefinition();


}
