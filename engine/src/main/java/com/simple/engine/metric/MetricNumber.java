package com.simple.engine.metric;

import java.io.IOException;
import java.util.List;

import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.Tag;
import com.simple.original.api.orchestrator.IMetricKey;

public final class MetricNumber extends Metric<MetricNumber>{

	static final Schema<MetricNumber> SCHEMA = new Schema<MetricNumber>() {

		final java.util.HashMap<String, Integer> fieldMap = new java.util.HashMap<String, Integer>();
		{
			fieldMap.put("key", 1);
			fieldMap.put("value", 2);
		}

		// schema methods

		public MetricNumber newMessage() {
			return new MetricNumber();
		}

		public Class<MetricNumber> typeClass() {
			return MetricNumber.class;
		}

		public String messageName() {
			return MetricNumber.class.getSimpleName();
		}

		public String messageFullName() {
			return MetricNumber.class.getName();
		}

		public boolean isInitialized(MetricNumber message) {
			return message.key != null;
		}

		public void mergeFrom(Input input, MetricNumber message) throws IOException {
			for (int number = input.readFieldNumber(this);; number = input.readFieldNumber(this)) {
				switch (number) {
				case 0:
					return;
				case 1:
					message.key = MetricKey.valueOf(input.readByteArray());
					break;
				case 2:
					message.value = input.readDouble();
					break;
				default:
					input.handleUnknownField(number, this);
				}
			}
		}

		@Override
		public String getFieldName(int number) {
			switch (number) {
			case 1:
				return "key";
			case 2:
				return "value";
			default:
				return null;
			}
		}

		public int getFieldNumber(String name) {
			final Integer number = fieldMap.get(name);
			return number == null ? 0 : number.intValue();
		}

		@Override
		public void writeTo(Output output, MetricNumber message) throws IOException {
			if (message.value != null) {
				output.writeDouble(2, message.value.doubleValue(), false);
			}
		}

	};

	

	@Tag(1)
	private IMetricKey key;

	@Tag(2)
	private Number value;
	
	
	public MetricNumber(List<Double> realValueList) {
	}

	public MetricNumber() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IMetricKey getKey() {
		return key;
	}

	@Override
	public byte[] toBytes() {
		LinkedBuffer buffer = LinkedBuffer.allocate(4096);
		return ProtobufIOUtil.toByteArray(this, SCHEMA, buffer);
	}

	@Override
	public Schema<MetricNumber> cachedSchema() {
		// TODO Auto-generated method stub
		return null;
	}
}
