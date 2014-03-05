package com.simple.original.test.utils;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;
import com.simple.original.client.utils.Protobuf;

public class TestProtobufUtilTest extends GWTTestCase {

	static final String ModuleName = "com.simple.original.Simple";

	@Override
	public String getModuleName() {
		return ModuleName;
	}

	@Test
	public void testBasic() {
		 Protobuf protobuf = Protobuf.create();
		// ProtobufBuilder builder = protobuf.loadFile("testproto.proto");
	}
}
