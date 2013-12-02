package com.simple.domain.export;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(value = {"staticCharts"})
public interface MetricCollectionMixin {

}
