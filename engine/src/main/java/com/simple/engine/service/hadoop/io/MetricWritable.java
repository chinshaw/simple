package com.simple.engine.service.hadoop.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.hadoop.io.WritableUtils;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.simple.engine.metric.IMetric;
import com.simple.engine.metric.IMetric.MimeType;

public class MetricWritable<M extends IMetric<?>> implements IMetricWritable {

	private static final Logger logger = Logger.getLogger(MetricWritable.class
			.getName());

	private IMetric metric;

	private MimeType mimeType = MimeType.JSON;

	public MetricWritable() {

	}

	public MetricWritable(IMetric<?> metric, MimeType mimeType) {
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
