package com.simple.radapter.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.simple.radapter.api.IRexpCollection;

public class RexpIntegerCollection implements IRexpCollection<Integer>{

	private Collection<Integer> values;
	
	public RexpIntegerCollection() {
	}
	
	@Override
	public Collection<Integer> getValue() {
		return values;
	}
	
	public void setValue(Integer[] values) {
		this.values = Arrays.asList(values);
	}
	
	public void setValue(List<Integer> values) {
		this.values = values;
	}

}
