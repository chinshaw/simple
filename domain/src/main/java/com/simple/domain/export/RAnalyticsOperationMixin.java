package com.simple.domain.export;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = {"newName", "dataProviders", "code"})
public interface RAnalyticsOperationMixin {

    @JsonIgnore
    public String getCode();
    
    
    @JsonProperty
    @JsonGetter(value = "rCode")
    public String getRCode();
    
}