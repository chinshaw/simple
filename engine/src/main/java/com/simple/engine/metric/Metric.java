package com.simple.engine.metric;

import java.util.List;

import com.simple.original.api.analytics.IAnalyticsOperationOutput;
import com.simple.original.api.analytics.IMetric;
import com.simple.original.api.analytics.IViolation;
import com.simple.radapter.protobuf.REXPProtos.REXP;

public class Metric implements IMetric {

	public Metric(REXP rexp) {
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public byte[] serialize() {
		// TODO Auto-generated method stub
		return null;
	}

}
