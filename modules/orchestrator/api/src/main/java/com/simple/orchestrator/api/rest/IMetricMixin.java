package com.simple.orchestrator.api.rest;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import com.simple.orchestrator.api.metric.MetricRaw;
import com.simple.orchestrator.api.metric.MetricString;

@JsonSubTypes({ @Type(value = MetricRaw.class, name = "MetricRaw"),
		@Type(value = MetricString.class, name = "MetricString") })
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "@class")
abstract class IMetricMixin {
	
}