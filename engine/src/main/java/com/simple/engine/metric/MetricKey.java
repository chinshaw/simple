package com.simple.engine.metric;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.hbase.util.Bytes;

public class MetricKey implements IMetricKey {

	private byte[] key = new byte[0];

	public MetricKey() {	
	}
	
	public MetricKey(byte[] bytes) {
		this.key = bytes;
	}
	
	public MetricKey(String key) {
		this.key = key.getBytes();
	}

	@Override
	public byte[] toBytes() {
		return key;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.write(key);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		in.readFully(key);
	}

	@Override
	public int compareTo(IMetricKey o) {
	    byte[] thisValue = this.key;
	    byte[] thatValue = o.toBytes();
	    return (Bytes.equals(thisValue,thatValue) ? -1 : (thisValue==thatValue ? 0 : 1));
	}

	public static IMetricKey valueOf(byte[] readByteArray) {
		return new MetricKey(readByteArray);
	}
}
