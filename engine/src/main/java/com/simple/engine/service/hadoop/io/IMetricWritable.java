package com.simple.engine.service.hadoop.io;

import org.apache.hadoop.io.Writable;

import com.simple.engine.api.IMetric;


public interface IMetricWritable extends Writable {

	
	/**
	 * This will convert the metric to a byte array, this will use
	 * default serialization which is protocol buffers 
	 * @return
	 */
	byte[] toBytes();
	
	public IMetric<?> getMetric();
	
	/**
	 * This is the mime type of the object when being written.
	 * @return
	 */
	String getMimeType();
}
