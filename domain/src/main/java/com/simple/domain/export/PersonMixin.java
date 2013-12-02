package com.simple.domain.export;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( value = {"taskLimitValue", "currentTasks"})
public interface PersonMixin {

    
}
