package com.simple.radapter;

import com.simple.radapter.api.IRexpDouble;
import com.simple.radapter.protobuf.REXPProtos.Rexp;

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

	@Override
	public Rexp getProtoBuf() {
		// TODO Auto-generated method stub
		return null;
	}
}
