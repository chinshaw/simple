package com.simple.engine.metric;

import org.apache.hadoop.io.WritableComparable;

import com.simple.engine.hbase.IHbaseKey;

public interface IMetricKey extends WritableComparable<IMetricKey>, IHbaseKey {

	byte[] toBytes();

}
