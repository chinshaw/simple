package com.simple.engine.metric;

import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import com.simple.original.api.analytics.IMetric;
import com.simple.original.api.analytics.IViolation;
import com.simple.radapter.protobuf.REXPProtos.REXP;

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
		return rexp.toByteArray();
	}
	
	public static Metric fromBytes(byte[] bytes) throws InvalidProtocolBufferException {
		return new Metric(REXP.parseFrom(bytes));
	}
}
