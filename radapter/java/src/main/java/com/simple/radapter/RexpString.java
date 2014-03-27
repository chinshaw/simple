package com.simple.radapter;

import com.simple.radapter.api.IRexpString;
import com.simple.radapter.protobuf.REXPProtos.Rexp;

public class RexpString implements IRexpString {

	
	private String value;
	
	public RexpString() {
		
	}
	
	public RexpString(String value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value;
	}

	@Override
	public Rexp getProtoBuf() {
		// TODO Auto-generated method stub
		return null;
	}

}
