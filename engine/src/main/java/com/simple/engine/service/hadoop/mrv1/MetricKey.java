package com.simple.engine.service.hadoop.mrv1;

public class MetricKey implements IMetricKey {

	private byte[] key_;
	
	public MetricKey(String key) {
		this.key_ = key.getBytes();
	}
	
	@Override
	public byte[] toBytes() {
		return key_;
	}

}
