package com.simple.engine.api;

import com.dyuproject.protostuff.Message;



public interface IMetric<T> extends Message<T>{

	public static enum MediaType {
		APPLICATION_JSON("application/json"),
		APPLICATION_XML("application/xml"),
		APPLICATION_PROTOBUF("application/x-protobuf"),
		PLAINTEXT("text/plain");
		
		private final String type;
		MediaType(String type) {
			this.type = type;
		}
		
		public String getType() {
			return type;
		}
	}
	
	IMetricKey getKey();
	
	byte[] toBytes();
}
