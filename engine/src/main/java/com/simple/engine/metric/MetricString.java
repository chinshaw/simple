package com.simple.engine.metric;

import com.dyuproject.protostuff.Tag;

public final class MetricString extends Metric {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Tag(2)
	private String value;


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
