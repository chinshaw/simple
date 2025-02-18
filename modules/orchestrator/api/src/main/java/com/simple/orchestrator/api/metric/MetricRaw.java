package com.simple.orchestrator.api.metric;

import java.io.IOException;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.Tag;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "@class")
public final class MetricRaw extends Metric<MetricRaw> {

	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = 3534483210955614885L;

	public static final Schema<MetricRaw> SCHEMA = new Schema<MetricRaw>() {

		final java.util.HashMap<String, Integer> fieldMap = new java.util.HashMap<String, Integer>();
		{
			fieldMap.put("key", 1);
			fieldMap.put("value", 2);
		}

		// schema methods

		public MetricRaw newMessage() {
			return new MetricRaw();
		}

		public Class<MetricRaw> typeClass() {
			return MetricRaw.class;
		}

		public String messageName() {
			return MetricRaw.class.getSimpleName();
		}

		public String messageFullName() {
			return MetricRaw.class.getName();
		}

		public boolean isInitialized(MetricRaw message) {
			return message.key != null;
		}

		public void mergeFrom(Input input, MetricRaw message) throws IOException {
			for (int number = input.readFieldNumber(this);; number = input.readFieldNumber(this)) {
				System.out.println("Number is " + number);
				switch (number) {
				case 0:
					return;
				case 1:
					message.key = MetricKey.from(input.readByteArray());
					break;
				case 2:
					message.value = input.readBytes().toByteArray();
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
		public void writeTo(Output output, MetricRaw message) throws IOException {
			if (message.value != null) {
				output.writeObject(1, message.getKey(), MetricKey.SCHEMA, false);
				output.writeBytes(2, ByteString.copyFrom(message.value), false);
			}
		}
	};
	
	@Tag(1)
	public MetricKey key;
	
	@Tag(2)
	private byte[] value;

	public MetricRaw() {
	}

	public MetricRaw(MetricKey key) {
		this.key = key;
	}

	public MetricRaw(MetricKey key, byte[] value) {
		this.key = key;
		this.value = value;
	}


	public MetricKey getKey() {
		return key;
	}
	
	public void setKey(MetricKey key) {
		this.key = key;
	}
	
	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

	@Override
	public byte[] toBytes() {
		LinkedBuffer buffer = LinkedBuffer.allocate(4096);
		return ProtobufIOUtil.toByteArray(this, SCHEMA, buffer);
	}

	public static MetricRaw fromBytes(byte[] readBytes) {
		MetricRaw raw = new MetricRaw();
		ProtobufIOUtil.mergeFrom(readBytes, raw, SCHEMA);
		return raw;
	}
	
	@Override
	public Schema<MetricRaw> cachedSchema() {
		return SCHEMA;
	}
}