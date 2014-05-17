package com.simple.orchestrator.metric;

import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Assert;
import org.junit.Test;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;

public class TestOperationOutputKey {

	
	@Test
	public void testSerialization() {
		OperationOutputKey m = new OperationOutputKey(12l, 59000l);
		LinkedBuffer lb = LinkedBuffer.allocate(4096);
		byte[] bytes = ProtobufIOUtil.toByteArray(m, OperationOutputKey.SCHEMA, lb);
		Assert.assertTrue(bytes.length > 2);
		
		OperationOutputKey unserialized = new OperationOutputKey();
		ProtobufIOUtil.mergeFrom(bytes, unserialized, OperationOutputKey.SCHEMA);
		
		Assert.assertTrue(Bytes.equals(m.toBytes(), unserialized.toBytes()));
		Assert.assertTrue(m.getTaskId().equals(unserialized.getTaskId()) );
		Assert.assertTrue(m.getOutputId().equals(unserialized.getOutputId()) );
		
		Assert.assertTrue(m.getTaskId() == 12l);
		Assert.assertTrue(m.getOutputId() == 59000l);
	}	
}