package com.simple.domain.model.metric;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.simple.domain.model.AnalyticsOperationOutput;
import com.simple.domain.model.RequestFactoryEntity;
import com.simple.original.api.analytics.IMetric;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Metric extends RequestFactoryEntity implements IMetric {

    /**
     * Serialization Id
     */
    private static final long serialVersionUID = 4409236293806246124L;

    private String name;
    
    private String context;
    
    @ManyToOne(optional = false, targetEntity = AnalyticsOperationOutput.class)
    @JoinColumn(name="origin_id")
    private AnalyticsOperationOutput origin;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Violation> violations = new ArrayList<Violation>();

    public Metric() {

    }

    public Metric(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean hasMetricViolations() {
        return (violations != null && violations.size() > 0);
    }

    @Override
    public List<Violation> getViolations() {
        return violations;
    }
    
    public void setViolations(List<Violation> violations) {
        this.violations = violations;
    }
    
    
    /**
     * Get the context for the output, this is really
     * future use for the output.
     */
    public String getContext() {
        return context;
    }
    
    /**
     * Optional context for the metric, this is future use if
     * we can come up with a way to chain operations.
     * @param context
     */
    public void setContext(String context) {
        this.context = context;
    }

    /**
     * This is the operation output that originally defined this metric
     * Typically this variable name is used to fetch the output.
     * @return
     */
    public AnalyticsOperationOutput getOrigin() {
        return origin;
    }

    /**
     * Setter for the origin of the metric, metrics are created from
     * outputs from operations, this is the operation output that defined
     * that operation so that it can be classified later on.
     * @param origin
     */
    public void setOrigin(AnalyticsOperationOutput origin) {
        this.origin = origin;
    }

    public byte[] toBytes() {
    	return new byte[1];
    }
    
    /**
     * Abstract method for the operation to retrieve its value as a string.
     * This is typically used to inject the value into the workspace for R.
     * @return
     */
    public abstract String getValueAsString();
    
    
    /**
     * Overridden from Object to give it some kind of context.
     */
    public String toString() {
        return "Metric name -> " + getName();
    }
    
}