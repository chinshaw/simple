package com.simple.original.api.analytics;

import java.io.Serializable;

public interface IMetric extends IHasViolations, Serializable {

	
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
	
	
	/**
	 * Id if the metric
	 * @return
	 */
	public Long getId();
	
	
    /**
     * Getter for the name of the object.
     * 
     * @return
     */
    public abstract String getName();

    /**
     * Set the name of the object.
     * 
     * @param name
     */
    public abstract void setName(String name);

    /**
     * Returns true if metric has violations.
     * 
     * @return
     */
    public abstract boolean hasMetricViolations();
	
	byte[] encode();
	
	byte[] encode(MimeType type);

}