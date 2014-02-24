package com.simple.radapter.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.simple.radapter.RAdapterFactory;
import com.simple.radapter.api.IRAdapter;
import com.simple.radapter.api.RAdapterException;
import com.simple.radapter.protobuf.REXPProtos.REXP;
import com.simple.radapter.protobuf.REXPProtos.REXP.RClass;

public class TestNativeAdapter {

	private static final Logger logger = Logger
			.getLogger(TestNativeAdapter.class.getName());

	private static final IRAdapter adapter = RAdapterFactory.createAdapter();

	@BeforeClass
	public static final void start() throws RAdapterException {
		adapter.connect();
	}

	@AfterClass
	public static final void stop() {
		adapter.disconnect();
	}

	@Test
	public void testEvalCommand() throws InvalidProtocolBufferException {
		logger.info("testEval");
		String command = "x <- 'meaning of life';";
		try {
			REXP rexp = adapter.exec(command);
			assertNotNull(rexp);
			REXP strValue = adapter.get("x");
			assertTrue(strValue.getStringValue(0).equals("meaning of life"));
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
				rexp = adapter.exec(command);
			} catch (InvalidProtocolBufferException e) {
				fail("Invalid protobuf returned");
			}
			assertNotNull(rexp);

			System.out.println("Rexp is " + rexp.getRclass() + " "
					+ rexp.getRealValue(0));

			assertTrue(rexp.getRealValue(0) == 42);
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
				rexp = adapter.exec(command);
			} catch (InvalidProtocolBufferException e) {
				fail("Invalid protobuf returned");
			}
			assertNotNull(rexp);

			System.out.println("Count of real " + rexp.getRealValueCount());

			for (int i = 0; i < rexp.getRealValueCount(); i++) {
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
				rexp = adapter.exec(command);
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
				rexp = adapter.exec(command);
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
		// String command =
		// "e <- c(\"red\", \"white\", \"red\", NA);  mydata <- data.frame(e);";
		// String command = "d <- c(1,2,3,4); mydata <- data.frame(d);";

		try {
			REXP rexp = null;
			try {
				rexp = adapter.exec(command);
			} catch (InvalidProtocolBufferException e) {
				fail("Invalid protobuf returned");
			}

			assertNotNull(rexp);
			assertTrue(rexp.getRclass() == RClass.VECSXP);
			System.out.println("count is " + rexp.getRexpValueCount());
			assertTrue(rexp.getRexpValueCount() == 3);
			assertTrue(rexp.getRexpValueList().get(0).getRclass() == RClass.REALSXP);
			assertTrue(rexp.getRexpValueList().get(1).getRclass() == RClass.STRSXP);
			assertTrue(rexp.getRexpValueList().get(2).getRclass() == RClass.LGLSXP);

			for (String str : rexp.getRexpValueList().get(1)
					.getStringValueList()) {
				System.out.println(str);
			}

		} catch (RAdapterException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetREXP() throws InvalidProtocolBufferException {
		String command = "e <- c(\"red\", \"white\", \"red\", NA);  mydata <- data.frame(e);";
		try {
			REXP rexp = null;
			try {
				rexp = adapter.exec(command);
			} catch (InvalidProtocolBufferException e) {
				fail("Invalid protobuf returned");
			}

			assertNotNull(rexp);

			rexp = adapter.get("e");
			assertNotNull(rexp);
			System.out.println("rexp is " + rexp.getRclass());

		} catch (RAdapterException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetInvalidREXP() throws InvalidProtocolBufferException {
	       String command = "e <- c(\"red\", \"white\", \"red\", NA);  mydata <- data.frame(e);";
	        try {
	            REXP rexp = null;
	            try {
	                rexp = adapter.exec(command);
	            } catch (InvalidProtocolBufferException e) {
	                fail("Invalid protobuf returned");
	            }

	            assertNotNull(rexp);

	            // l does not exit
	            rexp = adapter.get("doesntExist");
	            assertNotNull(rexp);
	            System.out.println("rexp is " + rexp.getRclass());

	        } catch (RAdapterException e) {
	            fail(e.getMessage());
	        }
	}

	@Test
	public void testPlot() throws IOException {

		final String command = "library(\"Cairo\");  "
				+ "Cairo(file = '/tmp/temp.png', width=1024, height = 800, type='png',  units = 'px', dpi = 'auto');"
				+ "cars <- c(1, 3, 6, 4, 9); "
				+ "plot(cars);";
		try {
			REXP rexp = null;
			try {
				rexp = adapter.exec(command);
			} catch (InvalidProtocolBufferException e) {
				fail("Invalid protobuf returned");
			}

			assertNotNull(rexp);
			
			// Get the plot.
			rexp = adapter.getPlot("/tmp/temp.png");
			assertTrue(rexp.getRclass() == RClass.RAWSXP);
			
			ByteString bytes = rexp.getRawValue();
			InputStream in = new ByteArrayInputStream(bytes.toByteArray());
			
			try {
				ImageInputStream iis = ImageIO.createImageInputStream(in);
				Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
				
				if (!iter.hasNext()) {
					fail("Image is of wrong type");
				}
				assertTrue(iter.next().getFormatName().equals("png"));
			} catch (IOException e) {
				fail(e.getMessage());
				
			} finally {
				in.close();
			}
		} catch (RAdapterException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testScript() throws InvalidProtocolBufferException, RAdapterException {
	    REXP rexp = adapter.exec(new File("/Users/chris/devel/workspace/simple/engine/src/test/resources/com/simple/engine/rscripts/BollingerScript.R"));
	 
	    System.out.println("Type is " + rexp.getRclass());
	    rexp = adapter.getPlot("/tmp/instrument.png");
	    System.out.println("Type is " + rexp.getRclass());
	}
	

}