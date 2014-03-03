package com.simple.engine.service.hadoop.mrv1;

import org.apache.hadoop.io.Writable;

public interface IMetricWritable extends Writable {

	
	byte[] toBytes();
}
