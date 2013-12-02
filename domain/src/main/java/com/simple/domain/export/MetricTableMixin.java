package com.simple.domain.export;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"metric"})
public interface MetricTableMixin {
    
    @JsonIgnore
    public Long getMetricCollectionId();
}
