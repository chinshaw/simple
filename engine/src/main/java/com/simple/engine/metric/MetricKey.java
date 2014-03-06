package com.simple.engine.metric;

public class MetricKey implements IMetricKey {

	private byte[] key;

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

	/**
	 * Create a key from bytes.
	 * @param bytes
	 * @return
	 */
	public static MetricKey valueOf(byte[] bytes) {
		return new MetricKey(bytes);
	}
}
