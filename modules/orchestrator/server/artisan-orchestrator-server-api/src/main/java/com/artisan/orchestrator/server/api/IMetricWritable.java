package com.artisan.orchestrator.server.api;

import org.apache.hadoop.io.Writable;

import com.simple.api.orchestrator.IMetric;


public interface IMetricWritable extends Writable {

	
	/**
	 * This will convert the metric to a byte array, this will use
	 * default serialization which is protocol buffers 
	 * @return
	 */
	byte[] toBytes();
	
	public IMetric getMetric();
	
	/**
	 * This is the mime type of the object when being written.
	 * @return
	 */
	String getMimeType();
}
