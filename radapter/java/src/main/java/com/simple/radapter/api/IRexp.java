package com.simple.radapter.api;

import com.simple.radapter.protobuf.REXPProtos.REXP;

public interface IRexp<V> {
	
	public V getValue();
	
	public REXP getProtoBuf();
	
}
