package com.simple.original.api.analytics;



public interface IMetricPlot extends IMetric {

    public String getImageFormat();
    
    public byte[] getImageData();
}