package com.simple.domain.model.metric;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.simple.original.api.analytics.IMetricInteger;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Access(AccessType.FIELD)
@Table(name= "metric_metricinteger")
public class MetricInteger extends MetricNumber implements IMetricInteger {

    /**
     * Serialization Id
     */
    private static final long serialVersionUID = -2117985373921467153L;

    private Integer value;

    public MetricInteger() {
        super();
    }

    public MetricInteger(Integer value) {
        this(null, value);
    }

    public MetricInteger(String name, Integer value) {
        super(name);
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public MetricInteger(String name) {
        super(name);
    }

    public String toString() {
        return "MetricInteger -> " + getValue().toString();
    }

    @Override
    public String getValueAsString() {
        return value.toString();
    }

	@Override
	public byte[] encode() {
		// TODO Auto-generated method stub
		return null;
	}

}
