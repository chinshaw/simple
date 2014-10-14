package com.simple.orchestrator.metric;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.simple.orchestrator.api.metric.MetricCollection;
import com.simple.orchestrator.api.metric.MetricKey;
import com.simple.orchestrator.api.metric.MetricRaw;
import com.simple.orchestrator.api.metric.MetricString;

public class TestMetricSerialization {

	
	@Test 
	public void testMetricString() {
		MetricString ms1 = new MetricString("Value 1");
		
		LinkedBuffer lb = LinkedBuffer.allocate(4096);
		byte[] bytes = ProtobufIOUtil.toByteArray(ms1, MetricString.SCHEMA, lb);
		MetricString unserialized = new MetricString();
		ProtobufIOUtil.mergeFrom(bytes, unserialized, MetricString.SCHEMA);
	}
	
	
	@Test
	public void testMetricCollection() {
		MetricCollection<MetricString> stringCollection = new MetricCollection<MetricString>();
		
		stringCollection.add(new MetricString("VALUE 1"));
		//stringCollection.add(new MetricString("Value 2"));
		
		LinkedBuffer lb = LinkedBuffer.allocate(4096);
		
		byte[] bytes = ProtobufIOUtil.toByteArray(stringCollection, stringCollection.cachedSchema(), lb);
		System.out.println("Buffer size is " + bytes.length);
		
		//MetricCollection collection = new MetricCollection();
		//ProtobufIOUtil.mergeFrom(bytes, collection, stringCollection.cachedSchema());
		//assert(unserialized.getValues().get(0).getStringValue().equals("VALUE 1"));
		//assert(unserialized.getValues().get(1).getStringValue().equals("VALUE 2"));
	}
	
	@Test
	public void testMetricRaw() {
		MetricRaw metric = new MetricRaw();
		metric.setKey(new MetricKey(1l));
		metric.setValue(new byte[]{3});
		
		System.out.println("Value of metric is " + metric.getKey().toString());
		
		LinkedBuffer lb = LinkedBuffer.allocate(4096);
		byte[] bytes = ProtobufIOUtil.toByteArray(metric, metric.cachedSchema(), lb);
		Assert.assertNotNull(bytes);
		
		MetricRaw unserialized = MetricRaw.fromBytes(bytes);
		Assert.assertNotNull(unserialized.getKey());
		
		// Check that the key values match
		Assert.assertTrue(Arrays.equals(unserialized.getKey().toBytes(), metric.getKey().toBytes()));
		// Check that the values match.
		Assert.assertTrue(Arrays.equals(unserialized.getValue(), new byte[]{3}));
		
		
	}
}
