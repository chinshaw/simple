package com.simple.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.simple.original.api.analytics.IRAnalyticsOperation;


/**
 * Representation of an R analytics oepration extends
 * {@link AnalyticsOperationImpl} and contains a few extra parameters for R
 * script execution. Code in this object is presentation formatted which means
 * that it is in html format. The get {@see getTextFormattedCode} will fetch the
 * runnable R script code in the format that is expected.
 * 
 * @author chinshaw
 * 
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class RAnalyticsOperation extends AnalyticsOperation implements IRAnalyticsOperation {

    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = -89026818716059352L;

    /**
     * This is the text code that is to be run
     */
    @Basic(fetch = FetchType.LAZY)
    @Column(name="code", columnDefinition="TEXT")
    private String code;

    /**
     * Default constructor
     */
    public RAnalyticsOperation() {
    }

    public RAnalyticsOperation(String name) {
        this();
        this.name = name;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }
    
    /**
     * Clone the RanalyticsOperation this mainly clones the code field. 
     * This is required because the code is lazily loaded.
     */
    public RAnalyticsOperation clone() throws CloneNotSupportedException {
        RAnalyticsOperation clone = (RAnalyticsOperation) super.clone();
        // This is fetched lazily so we have to call get.
        clone.setCode(getCode());

        return clone;
    }
}