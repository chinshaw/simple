package com.simple.domain.model.metric;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import com.simple.original.api.analytics.IMetricNumber;

@MappedSuperclass
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class MetricNumber extends Metric implements IMetricNumber {

    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = -4416394505752770209L;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name="metric_metricnumber_ranges")
    private List<NumberRange> ranges = new ArrayList<NumberRange>();
    

    public MetricNumber() {
    }

    public MetricNumber(String name) {
        super(name);
    }

    public List<NumberRange> getRanges() {
        return ranges;
    }

    public void setRanges(List<NumberRange> ranges) {
        this.ranges = ranges;
    }
    
    public abstract Number getValue();
    
    
    @Override
    public MetricNumber clone() throws CloneNotSupportedException{
        MetricNumber clone = (MetricNumber) super.clone();
        clone.setRanges(new ArrayList<NumberRange>());
        return clone;
        
    }
}