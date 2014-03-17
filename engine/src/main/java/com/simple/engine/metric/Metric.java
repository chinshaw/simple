package com.simple.engine.metric;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import com.dyuproject.protostuff.Message;
import com.simple.original.api.orchestrator.IMetric;



/**
 * This is the base metric class and all other metrics inherit from this. It extends
 * the api's IMetric class for uses in other packages along with the Message<T> for
 * support with protostuff. The Message<T> interface supports serialization using the 
 * protostuff api. 
 * 
 * NOTE!! If you update this api or add another metric class you have to update the
 * corresponding class in the ui's rest packages. This is needed for rest serialization.
 * An example of this is if you add another class that extends Metric you need to add
 * another @JsonSubTypes.
 * @author chris
 *
 * @param <T>
 */
@JsonSubTypes({@Type(value = MetricRaw.class, name="MetricRaw"), @Type(value=MetricString.class, name="MetricString")})
@JsonTypeInfo(use=Id.NAME, include=As.PROPERTY, property="@class")
public abstract class Metric<T extends IMetric<T>> implements IMetric<T>, Message<T>, Serializable  {

	
	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = 8227819619655788172L;

	public Metric() {

	}
	
}
