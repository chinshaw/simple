package com.simple.domain.export;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.simple.domain.metric.MetricDouble;
import com.simple.domain.metric.MetricInteger;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ 
    @JsonSubTypes.Type(value = MetricDouble.class, name = "MetricNumber"),
    @JsonSubTypes.Type(value = MetricDouble.class, name = "MetricDouble"),
    @JsonSubTypes.Type(value = MetricInteger.class, name = "MetricInteger")})
public interface MetricNumberMixin {

    @JsonIgnore
    public Double getDoubleValue();
}
