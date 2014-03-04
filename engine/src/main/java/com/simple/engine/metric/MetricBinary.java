package com.simple.engine.metric;

import com.dyuproject.protostuff.Tag;

public class MetricBinary extends Metric {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6921696736360331251L;

	@Tag(2)
	private byte[] value;

	public MetricBinary() {
	}
	
	public MetricBinary(byte[] value) {
		this.value = value;
	}
	

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}
}
