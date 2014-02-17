package com.simple.radapter.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.protobuf.InvalidProtocolBufferException;
import com.simple.radapter.AdapterFactory;
import com.simple.radapter.api.IRAdapter;
import com.simple.radapter.api.IRexp;
import com.simple.radapter.api.IRexpDouble;
import com.simple.radapter.api.RAdapterException;
import com.simple.radapter.protobuf.REXPProtos.REXP;

public class TestNativeAdapter {

	private static final Logger logger = Logger
			.getLogger(TestNativeAdapter.class.getName());

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

			System.out.println("Rexp is " + rexp.getValue());

			String question = adapter.getString("question");
			assertTrue(question.equals("meaning of life"));

			IRexpDouble answer = (IRexpDouble) adapter.get("answer");
			assertTrue(answer.getValue() == 42);

			logger.info("Question " + question + " answer " + answer);

		} catch (RAdapterException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testPb() {
		logger.info("testPb");
		String command = "question <- 'meaning of life'; answer <- 42;";
		try {
			REXP rexp = null;
			try {
				rexp = adapter.executeScript(command);
			} catch (InvalidProtocolBufferException e) {
				fail("Invalid protobuf returned");
			}
			assertNotNull(rexp);
			
			System.out.println("Rexp is " + rexp.getRclass() + " "
					+ rexp.getRealValue(0));

			assertTrue(rexp.getRealValue(0)== 42);
		} catch (RAdapterException e) {
			fail(e.getMessage());
		}
	}
	
	
	@Test
	public void testRealArray() {
		logger.info("testRealArray");
		String command = "question <- 'meaning of life'; answer <- c(4, 2);";
		try {
			REXP rexp = null;
			try {
				rexp = adapter.executeScript(command);
			} catch (InvalidProtocolBufferException e) {
				fail("Invalid protobuf returned");
			}
			assertNotNull(rexp);
			
			System.out.println("Count of real " + rexp.getRealValueCount());
			
			for ( int i = 0; i < rexp.getRealValueCount(); i++) {
				System.out.println("rexp value is " + rexp.getRealValue(i));
			}
			
			assertTrue(rexp.getRealValue(0) == 4);
			assertTrue(rexp.getRealValue(1) == 2);

		} catch (RAdapterException e) {
			fail(e.getMessage());
		}
	}
	
	
	@Test
	public void testStringArray() {
		logger.info("testStringArray");
		String command = "question <- c('meaning', 'of', 'life');";
		try {
			REXP rexp = null;
			try {
				rexp = adapter.executeScript(command);
			} catch (InvalidProtocolBufferException e) {
				fail("Invalid protobuf returned");
			}
			assertNotNull(rexp);
			
			assertTrue(rexp.getStringValueCount() > 0);
			System.out.println("rexp value " + rexp.getStringValue(0));
			System.out.println("rexp value " + rexp.getStringValue(1));
			System.out.println("rexp value " + rexp.getStringValue(2));
			
		} catch (RAdapterException e) {
			fail(e.getMessage());
		}
	}
	
	
	@Test
	public void testList() {
		logger.info("testStringArray");
		String command = "question <- c('meaning', 'of', 'life');";
		try {
			REXP rexp = null;
			try {
				rexp = adapter.executeScript(command);
			} catch (InvalidProtocolBufferException e) {
				fail("Invalid protobuf returned");
			}
			assertNotNull(rexp);
			
			assertTrue(rexp.getStringValueCount() > 0);
			
		} catch (RAdapterException e) {
			fail(e.getMessage());
		}
	}
	
	
	@Test
	public void testDataFrame() {
		logger.info("testDataFrame");
		String command = "d <- c(1,2,3,4); e <- c(\"red\", \"white\", \"red\", NA); f <- c(TRUE,TRUE,TRUE,FALSE); mydata <- data.frame(d,e,f);";
		//String command = "d <- c(1,2,3,4); mydata <- data.frame(d);";
		
		try {
			REXP rexp = null;
			try {
				rexp = adapter.executeScript(command);
			} catch (InvalidProtocolBufferException e) {
				fail("Invalid protobuf returned");
			}
			
			assertNotNull(rexp);
			System.out.println("TYpe is " + rexp.getRclass());
			for (REXP child : rexp.getRexpValueList()) {
				System.out.println("TYpe is " + child.getRclass());
			}
		} catch (RAdapterException e) {
			fail(e.getMessage());
		}
	}
}