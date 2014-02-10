package com.simple.radapter;

import com.simple.radapter.api.IRexpDouble;

public class RexpDouble implements IRexpDouble {

	private Double value;
	
	public RexpDouble(double value) {
		this.value = value;
	}
	
	@Override
	public Double getValue() {
		return value;
	}
	
	public void setDouble(double value) {
		this.value = value;
	}
}
