package com.simple.api.orchestrator;



public interface IMetricPlot extends IMetric {

    public String getImageFormat();
    
    public byte[] getImageData();
}