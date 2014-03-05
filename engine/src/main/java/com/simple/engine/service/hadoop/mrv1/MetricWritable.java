package com.simple.engine.service.hadoop.mrv1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.simple.original.api.analytics.IMetric;

public class MetricWritable<M extends IMetric> implements IMetricWritable {

	private IMetric metric;

	private String mimeType;

	
	public MetricWritable(IMetric metric, String mimeType) {
		this.metric = metric;
		this.mimeType = mimeType;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.write(metric.encode());
	}

	@Override
	public void readFields(DataInput in) throws IOException {
	}

	@Override
	public byte[] toBytes() {
		return metric.encode();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Set the mime type this can be "application/x-protobuf",
	 * "application/json", or "application/xml"
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
}
