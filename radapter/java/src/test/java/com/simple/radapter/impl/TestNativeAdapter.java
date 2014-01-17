package com.simple.radapter.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.simple.radapter.NativeAdapter;
import com.simple.radapter.api.IRAdapter;
import com.simple.radapter.api.IRexpString;
import com.simple.radapter.exceptions.RAdapterException;

public class TestNativeAdapter {

	
	private IRAdapter adapter = new NativeAdapter();
	
	@Test
	public void testAssignString() throws RAdapterException {
		adapter.connect();
		
		String value = "testAssignString";
		long exp = adapter.setString(value);
		adapter.assign("test1", exp, IRAdapter.GLOBAL_ENVIRONMENT);
		
		IRexpString testValue = adapter.getString("test1");
		System.out.println("testValue == " + testValue.getValue());
		assertTrue(testValue.getValue().equals(value));
	}
}
