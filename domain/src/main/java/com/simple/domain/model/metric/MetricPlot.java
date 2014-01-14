package com.simple.domain.model.metric;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.simple.original.api.analytics.IMetricPlot;

/**
 * Not sure if a chart should actually be a metric but we will try it and see if
 * it works out. TODO see if this is a correct use of metric.
 * 
 * Should use Metric to store your charts.
 * 
 * @author chinshaw
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Access(AccessType.FIELD)
@Table(name = "metric_metricplot")
public class MetricPlot extends Metric implements IMetricPlot {

    /**
     * Serialization ID
     */
    private static final long serialVersionUID = 3285655467485579605L;

    private String plotUrl = null;
    
    private String imageFormat;
    
    @Transient
    private transient byte[] imageData;
    
    public MetricPlot() {
        this(null, null);
    }
    
    public MetricPlot(String name) {
        super(name);
    }
    
    public MetricPlot(String name, String plotUrl) {
        super(name);
        this.plotUrl = plotUrl;
    }
    

    public String getPlotUrl() {
        return plotUrl;
    }
    
    protected void setPlotUrl(String plotUrl) {
        this.plotUrl = plotUrl;
    }
 
    @Override
    public String getImageFormat() {
        return imageFormat;
    }
    
    public void setImageFormat(String imageFormat) {
        this.imageFormat = imageFormat;
    }
    
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    @Override
    public byte[] getImageData() {
        return imageData;
    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public String getValueAsString() {
        return "N/A";
    }
}
