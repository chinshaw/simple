package com.simple.radapter.impl;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.simple.radapter.AdapterFactory;
import com.simple.radapter.NativeAdapter;
import com.simple.radapter.api.IRAdapter;
import com.simple.radapter.api.IRexpString;
import com.simple.radapter.api.RAdapterException;

public class TestNativeAdapter {

	
	private static final IRAdapter adapter = AdapterFactory.createAdapter();
	
	@BeforeClass
	public static final void start() throws RAdapterException {
		adapter.connect();
	}
	
	@AfterClass
	public static final void stop() {
		adapter.disconnect();
	}
	
	@Test
	public void testAssignString() throws RAdapterException {
		String value = "testAssignString";
		//long exp = adapter.setString(value);
		//adapter.assign("test1", exp, IRAdapter.GLOBAL_ENVIRONMENT);
		
		adapter.setString("test", value);
		
		String testValue = adapter.getString("test1");
		System.out.println("testValue == " + testValue);
		assertTrue(testValue.equals(value));
	}
	
	@Test
	public void testEveal() {
		
	}
	
	
}