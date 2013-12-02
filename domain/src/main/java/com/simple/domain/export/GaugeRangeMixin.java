package com.simple.domain.export;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonIgnoreProperties(value = {"min", "max"})
public interface GaugeRangeMixin {

    
    @JsonGetter("minimum")
    public Double getMinimum();
    
    @JsonSetter("minimum")
    public void setMinimum(Double min);
    
    @JsonGetter("maximum")
    public Double getMaximum();
    
    @JsonSetter("maximum")
    public void setMaximum(Double max);
    
    
    
}
