package com.simple.engine.metric;

import java.util.List;

import com.simple.original.api.analytics.IMetric;
import com.simple.original.api.analytics.IViolation;
import com.simple.radapter.protobuf.REXPProtos.REXP;

public class Metric implements IMetric {
	
	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = -5584219143925575764L;
	
	private REXP rexp;
	
	public Metric(REXP rexp) {
		this.rexp = rexp;
	}

	@Override
	public List<? extends IViolation> getViolations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasMetricViolations() {
		return false;
	}

	@Override
	public byte[] serialize() {
		if (rexp == null) {
			return null;
		}
		return rexp.toByteArray();
	}
}
