package com.simple.domain.export;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"version"})
public interface RequestFactoryEntityMixin  {

    
}
