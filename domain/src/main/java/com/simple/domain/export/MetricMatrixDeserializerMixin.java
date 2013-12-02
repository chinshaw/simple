package com.simple.domain.export;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"id", "version", "name", "context", "violations", "rows", "origin"})
public interface MetricMatrixDeserializerMixin {
}
