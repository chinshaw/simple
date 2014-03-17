package com.simple.original.api.orchestrator;

import java.io.Serializable;


public interface IMetric extends Serializable {
	
	IMetricKey getKey();
	
	byte[] toBytes();
}
