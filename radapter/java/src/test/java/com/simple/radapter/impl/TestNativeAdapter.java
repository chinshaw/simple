package com.simple.radapter.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.simple.radapter.AdapterFactory;
import com.simple.radapter.api.IRAdapter;
import com.simple.radapter.api.IRexp;
import com.simple.radapter.api.IRexpDouble;
import com.simple.radapter.api.RAdapterException;

public class TestNativeAdapter {

	private static final Logger logger = Logger.getLogger(TestNativeAdapter.class.getName());
	
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
		logger.info("testAssignString");
		String value = "testAssignString";
		adapter.setString("test1", value);
		
		String testValue = adapter.getString("test1");
		assertTrue(testValue.equals(value));
	}
	
	@Test
	public void testEvalCommand() {
		logger.info("testEval");
		String command = "x <- 'meaning of life';";
		try {
			IRexp<?> rexp = adapter.execCommand(command);
			assertNotNull(rexp);
			String strValue = adapter.getString("x");
			assertTrue(strValue.equals("meaning of life"));
		} catch (RAdapterException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testEvalScript() {
		logger.info("testEval");
		String command = "question <- 'meaning of life'; answer <- 42;";
		try {
			IRexp<?> rexp = adapter.execScript(command);
			assertNotNull(rexp);
			
			
			System.out.println("Rexp is "+ rexp.getValue());
			
			String question = adapter.getString("question");
			assertTrue(question.equals("meaning of life"));
			
			IRexpDouble answer = (IRexpDouble) adapter.get("answer");
			assertTrue(answer.getValue() == 42);
			
			logger.info("Question " + question + " answer " + answer);
			
		} catch (RAdapterException e) {
			fail(e.getMessage());
		}
	}
}