package com.simple.engine.metric;


public class MetricBinary extends Metric {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6921696736360331251L;

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
