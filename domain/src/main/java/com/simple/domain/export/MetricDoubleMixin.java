package com.simple.domain.export;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.simple.domain.model.metric.NumberRange;

@JsonIgnoreProperties(value = {"highRange", "midRange", "lowRange", "totalRange"} )
public interface MetricDoubleMixin {

    
    public void setHighRange(NumberRange range);
}
