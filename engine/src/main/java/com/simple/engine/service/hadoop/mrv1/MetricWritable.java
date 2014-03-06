package com.simple.engine.service.hadoop.mrv1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.simple.engine.metric.IMetric;
import com.simple.engine.metric.IMetric.MimeType;


public class MetricWritable<M extends IMetric> implements IMetricWritable {

	private IMetric metric;

	private MimeType mimeType;

	
	public MetricWritable(IMetric metric, MimeType mimeType) {
		this.metric = metric;
		this.mimeType = mimeType;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.write(metric.encode(mimeType));
	}

	@Override
	public void readFields(DataInput in) throws IOException {
	}

	@Override
	public byte[] toBytes() {
		return metric.encode(mimeType);
	}

	/**
	 * {@inheritDoc}
	 */
	public MimeType getMimeType() {
		return mimeType;
	}

	/**
	 * Set the mime type this can be "application/x-protobuf",
	 * "application/json", or "application/xml"
	 */
	public void setMimeType(MimeType mimeType) {
		this.mimeType = mimeType;
	}
}
