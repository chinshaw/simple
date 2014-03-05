package com.simple.domain.model.metric;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.simple.original.api.analytics.IMetricDouble;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Access(AccessType.FIELD)
@Table(name = "metric_metricdouble")
public class MetricDouble extends MetricNumber implements IMetricDouble {

    private Double value;

    /**
     * Serialization Id
     */
    private static final long serialVersionUID = 3817011106235914544L;

    public MetricDouble() {
        this(null, null);
    }
    
    public MetricDouble(String name) {
        this(name, null);
    }

    public MetricDouble(Double value) {
        this(null, value);
    }

    public MetricDouble(String name, Double value) {
        super(name);
        this.value = value;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public void setValue(Double value) {
        this.value = value;
    }
    
    public String toString() {
        return "MetricDouble -> " + getValue().doubleValue();
    }

    @Override
    public String getValueAsString() {
        return value.toString();
    }
    
    @Override
    public MetricDouble clone() throws CloneNotSupportedException {
        return (MetricDouble) super.clone();
    }

	@Override
	public byte[] encode() {
		// TODO Auto-generated method stub
		return null;
	}
}
