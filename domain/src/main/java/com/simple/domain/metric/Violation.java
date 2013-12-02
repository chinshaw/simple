package com.simple.domain.metric;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.simple.domain.DatastoreObject;
import com.simple.original.api.analytics.IViolation;

@Entity
@Access(AccessType.FIELD)
public class Violation extends DatastoreObject implements IViolation {

    /**
     * Serialization Id
     */
    private static final long serialVersionUID = -4513852617297709748L;

    /**
     * This is the severity of the violation, this can be any integer
     */
    @Enumerated(EnumType.ORDINAL)
    private Severity severity;

    private Integer subgroup;

    private String detail = null;

    private String ruleName = null; // WESTGAURD 1

    public Violation() {
    }

    /**
     * Return severity of the metric
     * 
     * @see com.simple.orginal.api.analytics.taskengine.shared.metric.IViolation#getSeverity()
     */
    @Override
    public Severity getSeverity() {
        return severity;
    }

    /**
     * Set severity of the metric
     * 
     * @see com.simple.orginal.api.analytics.taskengine.shared.metric.IViolation#setSeverity(java.lang.Integer
     *      )
     */
    @Override
    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    /**
     * Set the severity from the index value of the severity value.
     * 
     * @see #setSeverity(com.simple.orginal.api.analytics.analytics.shared.metric.IViolation.Severity)
     * @param severity
     *            The integer index of the severity
     */
    public void setSeverity(Integer severity) {
        // setSeverity(Severity.values()[severity]);
    }

    /**
     * Get the detail of the violation
     * 
     * @see com.simple.orginal.api.analytics.taskengine.shared.metric.IViolation#getDetail()
     */
    @Override
    public String getDetail() {
        return detail;
    }

    /**
     * Set detail of the violation
     * 
     * @see com.simple.orginal.api.analytics.taskengine.shared.metric.IViolation#setDetail(java.lang.String)
     */
    @Override
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * Get the rule name for the violation
     * 
     * @see com.simple.orginal.api.analytics.taskengine.shared.metric.IViolation#getRuleName()
     */
    @Override
    public String getRuleName() {
        return ruleName;
    }

    /**
     * Set the rul name for the violation.
     * 
     * @see com.simple.orginal.api.analytics.taskengine.shared.metric.IViolation#setRuleName(java.lang.String
     *      )
     */
    @Override
    public void setRuleName(String rule) {
        this.ruleName = rule;
    }

    /**
     * Get the subgroup for the violation.
     * 
     * @see com.simple.orginal.api.analytics.taskengine.shared.metric.IViolation#getSubgroup()
     */
    @Override
    public Integer getSubgroup() {
        return subgroup;
    }

    /**
     * 
     * Set the subgroup for the violation
     * 
     * @see com.simple.orginal.api.analytics.taskengine.shared.metric.IViolation#setSubgroup(java.lang.Integer
     *      )
     */
    @Override
    public void setSubgroup(Integer subgroup) {
        this.subgroup = subgroup;
    }

}
