package com.simple.engine.metric;

import java.io.IOException;

import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.Tag;

public final class MetricString extends Metric implements Message<MetricString> {

	static final Schema<MetricString> SCHEMA = new Schema<MetricString>() {

		final java.util.HashMap<String, Integer> fieldMap = new java.util.HashMap<String, Integer>();
		{
			fieldMap.put("key", 1);
			fieldMap.put("value", 2);
		}

		// schema methods

		public MetricString newMessage() {
			return new MetricString();
		}

		public Class<MetricString> typeClass() {
			return MetricString.class;
		}

		public String messageName() {
			return MetricString.class.getSimpleName();
		}

		public String messageFullName() {
			return MetricString.class.getName();
		}

		public boolean isInitialized(MetricString message) {
			return message.key != null;
		}

		public void mergeFrom(Input input, MetricString message) throws IOException {
			for (int number = input.readFieldNumber(this);; number = input.readFieldNumber(this)) {
				switch (number) {
				case 0:
					return;
				case 1:
					message.key = MetricKey.valueOf(input.readByteArray());
					break;
				case 2:
					message.value = input.readByteArray();
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
		public void writeTo(Output output, MetricString message) throws IOException {
			if (message.value != null) {
				output.writeByteArray(2, message.value, true);
			}
		}

	};

	@Tag(1)
	private IMetricKey key;

	@Tag(2)
	private byte[] value;

	public MetricString() {

	}

	public MetricString(IMetricKey key) {
		this.key = key;
	}
	
	public MetricString(byte[] value) {
		this.value = value;
	}
	
	public MetricString(String value) {
		this.value = value.getBytes();
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}
	
	public String getStringValue() {
		return new String(value);
	}
	
	public void setStringValue(String value) {
		this.value = value.getBytes();
	}

	@Override
	public IMetricKey getKey() {
		return key;
	}

	// message method

	public Schema<MetricString> cachedSchema() {
		return SCHEMA;
	}

	@Override
	public byte[] encode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] encode(MimeType type) {
		// TODO Auto-generated method stub
		return null;
	}

}
