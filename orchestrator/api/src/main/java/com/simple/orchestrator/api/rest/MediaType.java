package com.simple.orchestrator.api.rest;

import java.util.Map;

/**
 * Override the jaxrs media type to add support for protocol buffers.
 * @author chris
 *
 */
public class MediaType extends javax.ws.rs.core.MediaType {
	
	public final static String APPLICATION_PROTOBUF = "application/x-protobuf";

    /** "application/xml" */
    public final static MediaType APPLICATION_PROTOBUF_TYPE = new MediaType("application","x-protobuf");

	public MediaType() {
		super();
	}

	public MediaType(String type, String subtype, Map<String, String> parameters) {
		super(type, subtype, parameters);
	}

	public MediaType(String type, String subtype) {
		super(type, subtype);
	}
    
    
}
