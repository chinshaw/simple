package com.simple.original.client.service.rest;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;



@JsonSubTypes({@Type(value=MetricRaw.class, name="MetricRaw")})
@JsonTypeInfo(use=Id.NAME, include=As.PROPERTY, property="@class")
public abstract class Metric {
	
}
