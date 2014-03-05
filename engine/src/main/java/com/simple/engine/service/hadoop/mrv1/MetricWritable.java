package com.simple.engine.service.hadoop.mrv1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.simple.original.api.analytics.IMetric;

public class MetricWritable<M extends IMetric> implements IMetricWritable {

	private IMetric metric;
	private byte[] messageBytes;

	public MetricWritable(IMetric metric) {
		this.metric = metric;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.write(metric.encode());
	}

	@Override
	public void readFields(DataInput in) throws IOException {
	    metric = null;
	    messageBytes = null;
	    int size = in.readInt();
	    if (size > 0) {
	      byte[] buf = new byte[size];
	      in.readFully(buf, 0, size);
	      messageBytes = buf;
	    }
	}

	@Override
	public byte[] toBytes() {
		return metric.encode();
	}
}
