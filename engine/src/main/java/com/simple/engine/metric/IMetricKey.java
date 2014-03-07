package com.simple.engine.metric;

import org.apache.hadoop.io.WritableComparable;

public interface IMetricKey extends WritableComparable<IMetricKey>{

	byte[] toBytes();

}
