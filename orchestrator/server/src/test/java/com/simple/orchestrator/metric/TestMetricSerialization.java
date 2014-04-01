package com.simple.orchestrator.metric;

import org.junit.Test;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.simple.orchestrator.metric.MetricCollection;
import com.simple.orchestrator.metric.MetricString;

public class TestMetricSerialization {

	
	@Test 
	public void testMetricString() {
		MetricString ms1 = new MetricString("Value 1");
		
		LinkedBuffer lb = LinkedBuffer.allocate(4096);
		byte[] bytes = ProtobufIOUtil.toByteArray(ms1, MetricString.SCHEMA, lb);
		System.out.println("Length is " + bytes.length);
		MetricString unserialized = new MetricString();
		ProtobufIOUtil.mergeFrom(bytes, unserialized, MetricString.SCHEMA);
		System.out.println("Value is " + unserialized.getStringValue());
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
}
