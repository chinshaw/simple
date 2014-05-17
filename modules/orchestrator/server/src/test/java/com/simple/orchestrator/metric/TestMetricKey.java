package com.simple.orchestrator.metric;

import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Assert;
import org.junit.Test;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;

public class TestMetricKey {

	
	
	/**
	 * This will crate a metric key convert it to a protocol buffer
	 * and then convert it back each time checking that the values match
	 * the expected value.
	 */
	@Test
	public void testProtobufSerialization() {
		MetricKey m = new MetricKey("FOOO");
		LinkedBuffer lb = LinkedBuffer.allocate(4096);
		byte[] bytes = ProtobufIOUtil.toByteArray(m, MetricKey.SCHEMA, lb);
		Assert.assertTrue(bytes.length > 2);
		
		MetricKey unserialized = new MetricKey();
		ProtobufIOUtil.mergeFrom(bytes, unserialized, MetricKey.SCHEMA);
		
		Assert.assertTrue(Bytes.equals(m.toBytes(), unserialized.toBytes()));
		Assert.assertTrue(Bytes.toString(m.toBytes()).equals("FOOO"));
	}
	
	@Test
	public void testCreate() {
		MetricKey m = new MetricKey("FOO");
		Assert.assertTrue(m.toBytes() != null);
		Assert.assertTrue(m.toBytes().length > 2);
		// Check that the value matches the testValue
		Assert.assertTrue(Bytes.equals(m.toBytes(), Bytes.toBytes("FOO")));
	}
}
