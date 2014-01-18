package com.simple.radapter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.simple.radapter.api.IRexp;
import com.simple.radapter.api.IRexpCollection;
import com.simple.radapter.api.IRexpInteger;

public class RexpIntegerCollection implements IRexpCollection<IRexpInteger>{

	private Collection<IRexpInteger> values;
	
	public RexpIntegerCollection() {
	}
	
	@Override
	public Collection<IRexpInteger> getValue() {
		return values;
	}
	
	public void setValue(IRexpInteger[] values) {
		this.values = Arrays.asList(values);
	}
	
	public void setValue(List<IRexpInteger> values) {
		this.values = values;
	}
}
