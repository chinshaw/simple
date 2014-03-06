package com.simple.engine.service.hadoop.mrv1;

import org.apache.hadoop.io.Writable;

import com.simple.engine.metric.IMetric.MimeType;


public interface IMetricWritable extends Writable {

	
	/**
	 * This will convert the metric to a byte array, this will use
	 * default serialization which is protocol buffers 
	 * @return
	 */
	byte[] toBytes();
	
	
	/**
	 * This is the mime type of the object when being written.
	 * @return
	 */
	MimeType getMimeType();
}
