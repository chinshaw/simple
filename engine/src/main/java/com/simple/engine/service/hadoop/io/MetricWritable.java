package com.simple.engine.service.hadoop.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.hadoop.io.WritableUtils;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.simple.engine.api.IMetric;
import com.simple.engine.api.IMetricWritable;
import com.simple.engine.api.MediaType;

public class MetricWritable<M extends IMetric<?>> implements IMetricWritable {

	private static final Logger logger = Logger.getLogger(MetricWritable.class
			.getName());

	private IMetric metric;

	private String mimeType = MediaType.APPLICATION_PROTOBUF;

	public MetricWritable() {

	}

	public MetricWritable(IMetric<?> metric, String mimeType) {
		this.metric = metric;
		this.mimeType = mimeType;
	}

	@Override
	public IMetric<?> getMetric() {
		return metric;
	}

	@Override
	public byte[] toBytes() {
		return metric.toBytes();
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

	@Override
	public void write(DataOutput out) throws IOException {
		byte[] bytes = metric.toBytes();
		WritableUtils.writeVInt(out, bytes.length);
		WritableUtils.writeString(out, metric.getClass().getName());
		out.write(bytes, 0, bytes.length);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		int newLength = WritableUtils.readVInt(in);
		String strClass = WritableUtils.readString(in);
		try {
			Class<?> clazz = Class.forName(strClass);
			metric = (IMetric<?>) clazz.newInstance();

			logger.info("Reading length " + newLength);
			byte[] bytes = new byte[newLength];
			in.readFully(bytes, 0, newLength);
			ProtobufIOUtil.mergeFrom(bytes, metric, metric.cachedSchema());
		} catch (Exception e) {
			throw new RuntimeException("Unable to convert from protocol buffer", e);
		}
	}
}
