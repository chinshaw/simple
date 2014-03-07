package com.simple.engine.metric;

import com.dyuproject.protostuff.Message;



public interface IMetric<T> extends Message<T>{

	public static enum MimeType {
		JSON("application/json"),
		XML("application/xml"),
		PROTO("application/x-protobuf"),
		TEXT("text/plain");
		
		private final String type;
		MimeType(String type) {
			this.type = type;
		}
		
		public String getType() {
			return type;
		}
	}
	
	IMetricKey getKey();
	
	byte[] toBytes();
}
