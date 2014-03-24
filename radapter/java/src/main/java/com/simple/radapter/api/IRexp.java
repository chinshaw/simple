package com.simple.radapter.api;

import com.simple.radapter.protobuf.Rexp;

public interface IRexp<V> {
	
	public V getValue();
	
	public Rexp getProtoBuf();
	
}
