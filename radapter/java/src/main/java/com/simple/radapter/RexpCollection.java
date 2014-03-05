package com.simple.radapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.simple.radapter.api.IRexp;
import com.simple.radapter.api.IRexpCollection;
import com.simple.radapter.protobuf.REXP;

public class RexpCollection<T extends IRexp<?>> extends Rexp implements IRexpCollection<T>{

	private List<T> values;
	
	public RexpCollection() {
		values = new ArrayList<T>();
	}
	
	@Override
	public Collection<T> getValue() {
		return values;
	}
	
	public void put(T object) {
		values.add(object);
	}

	public void setValue(List<T> values) {
		this.values = values;
	}

	@Override
	public REXP getProtoBuf() {
		// TODO Auto-generated method stub
		return null;
	}
}
