package com.simple.engine.metric;



public interface IMetric {

	public static enum MimeType {
		JSON("application/json"),
		XML("application/xml"),
		PROTO("application/x-protobuf");
		
		private final String type;
		MimeType(String type) {
			this.type = type;
		}
		
		public String getType() {
			return type;
		}
	}
	
	IMetricKey getKey();
	
	byte[] encode();
	
	byte[] encode(MimeType type);
}
