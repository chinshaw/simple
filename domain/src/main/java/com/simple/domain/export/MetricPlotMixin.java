package com.simple.domain.export;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public interface MetricPlotMixin {

    @JsonGetter("chartPath")
    public void getPlotUrl();
    
    @JsonSetter("chartPath")
    public void setPlotUrl(String plotUrl);
}
