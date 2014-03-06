package com.simple.domain.model.metric;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.simple.original.api.analytics.IMetricString;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Access(AccessType.FIELD)
@Table(name = "metric_metricstring")
public class MetricString extends Metric implements IMetricString {

    /**
     * Serialization ID
     */
    private static final long serialVersionUID = -8343386316459089723L;
    
    private String value;

    /**
     * Default constructor.
     */
    public MetricString() {
        this(null, null);
    }
    
    public MetricString(String name) {
       this(name, null);
    }

    /**
     * Constructor with name and value params.
     * 
     * @param name
     * @param value
     */
    public MetricString(String name, String value) {
        super(name);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return "CLS -> MetricString : Name -> " + getName() + ": Value -> " + value;
    }

    @Override
    public String getContext() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getValueAsString() {
        return value;
    }
}
