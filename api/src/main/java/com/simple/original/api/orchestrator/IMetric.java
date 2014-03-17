package com.simple.original.api.orchestrator;

import java.io.Serializable;


public interface IMetric<T> extends Serializable  {
	
	IMetricKey getKey();
	
	byte[] toBytes();
}
