package com.simple.engine.api;

import com.dyuproject.protostuff.Message;



public interface IMetric<T> extends Message<T>{
	
	IMetricKey getKey();
	
	byte[] toBytes();
}
