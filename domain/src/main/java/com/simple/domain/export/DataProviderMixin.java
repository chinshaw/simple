package com.simple.domain.export;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.simple.domain.model.RDataProvider;
import com.simple.domain.model.SqlDataProvider;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ 
    @JsonSubTypes.Type(value = SqlDataProvider.class, name = "SqlDataProvider"),
    @JsonSubTypes.Type(value = RDataProvider.class, name = "RDataProvider"),})
public interface DataProviderMixin {

}
