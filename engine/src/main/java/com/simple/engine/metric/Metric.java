package com.simple.engine.metric;

import java.util.List;

import com.dyuproject.protostuff.JsonIOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.google.protobuf.InvalidProtocolBufferException;
import com.simple.original.api.analytics.IMetric;
import com.simple.original.api.analytics.IViolation;
import com.simple.radapter.protobuf.REXP;

public class Metric implements IMetric {
	

	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = -5584219143925575764L;
	
	private REXP rexp;
	
	public Metric() {
		
	}
	
	public Metric(REXP rexp) {
		this.rexp = rexp;
	}

	@Override
	public List<? extends IViolation> getViolations() {
		return null;
	}

	@Override
	public Long getId() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void setName(String name) {
		
	}

	@Override
	public boolean hasMetricViolations() {
		return false;
	}
	
	public REXP getRexp() {
		return rexp;
	}

	@Override
	public byte[] encode() {
		if (rexp == null) {
			return null;
		}
		LinkedBuffer buffer = LinkedBuffer.allocate(4096);
		return ProtobufIOUtil.toByteArray(rexp, REXP.getSchema(), buffer);
	}
	
	
	public byte[] encode(MimeType type) {
		if (type == MimeType.JSON) {
			return JsonIOUtil.toByteArray(rexp, REXP.getSchema(), false);
		} 
		return encode();
	}
	
	public static Metric fromBytes(byte[] bytes) throws InvalidProtocolBufferException {
		REXP rexp = new REXP();
		ProtobufIOUtil.mergeFrom(bytes, rexp, REXP.getSchema());
		return new Metric(rexp);
	}
}
