package com.simple.engine.service.hadoop.mrv1;

import org.apache.hadoop.io.Writable;

public interface IMetricWritable extends Writable {

	
	/**
	 * This will convert the metric to a byte array, this will use
	 * default serialization which is protocol buffers 
	 * @return
	 */
	byte[] toBytes();
}
