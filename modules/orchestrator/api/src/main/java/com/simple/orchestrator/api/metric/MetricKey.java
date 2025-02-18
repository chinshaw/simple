package com.simple.orchestrator.api.metric;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.hadoop.hbase.util.HasThread;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableUtils;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.Tag;
import com.simple.api.orchestrator.IMetricKey;

/**
 * This is a generic key that has it's basic value
 * 
 * @author chris
 * 
 */
@XmlRootElement
public class MetricKey implements IMetricKey, WritableComparable<IMetricKey>, Message<MetricKey> {

	public static final char VALUE_SEPARATOR = ':';
	
	
	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = -8207410203282996916L;

	/**
	 * Embedded schema fro faster runtime processing
	 */
	public static final Schema<MetricKey> SCHEMA = new Schema<MetricKey>() {

		final java.util.HashMap<String, Integer> fieldMap = new java.util.HashMap<String, Integer>();
		{
			fieldMap.put("value", 1);
		}

		public MetricKey newMessage() {
			return new MetricKey();
		}

		public Class<MetricKey> typeClass() {
			return MetricKey.class;
		}

		public String messageName() {
			return MetricRaw.class.getSimpleName();
		}

		public String messageFullName() {
			return MetricRaw.class.getName();
		}

		public boolean isInitialized(MetricKey message) {
			return message.value != null;
		}

		public void mergeFrom(Input input, MetricKey message) throws IOException {
			for (int number = input.readFieldNumber(this);; number = input.readFieldNumber(this)) {
				switch (number) {
				case 0:
					return;
				case 1:
					message.value = input.readByteArray();
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
		public void writeTo(Output output, MetricKey message) throws IOException {
			if (message.value != null) {
				output.writeBytes(1, ByteString.copyFrom(message.value), false);
			}
		}
	};

	@Tag(1)
	private byte[] value;
	
	public MetricKey() {
	}

	public MetricKey(byte[] bytes) {
		this.value = bytes;
	}

	/**
	 * You should use the long key whenever possible because
	 * it is dramatically smaller.
	 * @param stringKey
	 */
	public MetricKey(String stringKey) {
		this.value = Bytes.toBytes(stringKey);
	}

	public MetricKey(Long longKey) {
		this.value = Bytes.toBytes(longKey);
	}
	
	@Override
	public byte[] toBytes() {
		return value;
	}
	
	protected void setValue(byte[] value) {
		this.value = value;
	}
	
	protected byte[] getValue() {
		return value;
	}

	/**
	 * This will first write the length of the value out to the stream and this
	 * must be read in before reading the value.
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		WritableUtils.writeVInt(out, value.length);
		out.write(value, 0, value.length);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		int newLength = WritableUtils.readVInt(in);
		value = new byte[newLength];
		in.readFully(value);
	}

	/**
	 * Simple method that will create and initialize a metric key from the byte array.
	 * @param readByteArray
	 * @return
	 */
	public static MetricKey from(byte[] readByteArray) {
		return new MetricKey(readByteArray);
	}

	
	/**
	 * Protostuff schema converter. This is used to convert the object
	 * to another serialized type.
	 */
	@Override
	public Schema<MetricKey> cachedSchema() {
		return SCHEMA;
	}

	/**
	 * Overriden compare to method, this will return 0 if the
	 * values are the same.
	 */
	@Override
	public int compareTo(IMetricKey o) {
		byte[] thisValue = this.value;
		byte[] thatValue = o.toBytes();
		return (Bytes.equals(thisValue, thatValue) ? -1 : (thisValue == thatValue ? 0 : 1));
	}
	
	/**
	 * Overridden way to view the metric key, this will print the value as a string.
	 * This converts the value to a string and prints it.
	 */
	@Override
	public String toString() {
		return new StringBuilder("MetricKey[").append(Bytes.toString(value)).append("]").toString();
	}
	
	/**
	 * This is a simple helper that will allow you to create a key statically
	 * using a byte[] value. It is equivilent to calling {@link #MetricKey(byte[])}
	 * 
	 * @param taskId
	 * @param opId
	 * @return Metric key with value set to value
	 */
	public static MetricKey create(byte[] value) {
		return new MetricKey(value);
	}
	
	
}
