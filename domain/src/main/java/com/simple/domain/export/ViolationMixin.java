package com.simple.domain.export;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"version"})
public interface ViolationMixin {

    
    @JsonIgnore
    public void setSeverity(Integer severity);
}
