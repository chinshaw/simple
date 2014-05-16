package com.simple.orchestrator.metric;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.Tag;
import com.simple.api.orchestrator.IMetricKey;

public final class MetricCollection<T extends Metric<T>> extends
		Metric<MetricCollection<T>> {

	final Schema<MetricCollection<T>> SCHEMA = new Schema<MetricCollection<T>>() {
		// schema methods

		public MetricCollection<T> newMessage() {
			return new MetricCollection<T>();
		}

		public Class<MetricCollection> typeClass() {
			return MetricCollection.class;
		}

		public String messageName() {
			return MetricCollection.class.getSimpleName();
		}

		public String messageFullName() {
			return MetricCollection.class.getName();
		}

		public boolean isInitialized(MetricCollection<T> message) {
			return true;
		}

		public void mergeFrom(Input input, MetricCollection<T> message)
				throws IOException {
			for (int number = input.readFieldNumber(this);; number = input
					.readFieldNumber(this)) {
				switch (number) {
				case 0:
					return;
				case 1:
					message.key = MetricKey.from(input.readByteArray());
					break;
				case 2:
					if (message.values == null) {
						message.values = new ArrayList<T>();
					}
					message.values
							.add(input.mergeObject(null, elementSchema()));
					break;

				default:
					input.handleUnknownField(number, this);
				}
			}
		}

		public void writeTo(Output output, MetricCollection<T> message)
				throws IOException {
			if (message.key != null) {
				output.writeByteArray(1, message.key.toBytes(), false);
			}
			if (message.values != null) {
				for (T value : message.values) {
					if (value != null) {
						Schema<T> schema = (Schema<T>) value.cachedSchema();
						output.writeObject(2, value, schema, true);
					}
				}
			}

		}

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

		final java.util.HashMap<String, Integer> fieldMap = new java.util.HashMap<String, Integer>();
		{
			fieldMap.put("key", 1);
			fieldMap.put("values", 2);
		}
	};

	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = -8297053011308850563L;

	@Tag(1)
	private IMetricKey key;

	@Tag(2)
	private List<T> values;

	private Class<T> collectionType;

	public MetricCollection() {
		this(new ArrayList<T>());
	}

	public MetricCollection(List<T> values) {
		this.values = values;
	}

	@Override
	public IMetricKey getKey() {
		return key;
	}

	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<T> getValues() {
		return values;
	}

	public void add(T toAdd) {
		values.add(toAdd);
	}

	public void setValues(List<T> values) {
		this.values = values;
	}

	@Override
	public Schema<MetricCollection<T>> cachedSchema() {
		return SCHEMA;
	}

	public Schema<T> elementSchema() {
		Schema<T> schema = null;
		ParameterizedType parameterizedType = (ParameterizedType) getClass()
				.getGenericSuperclass();
		System.out.println("parameterized type is " + parameterizedType.toString());
		Class<T> elementClass = (Class<T>) parameterizedType
				.getActualTypeArguments()[0];
		try {
			schema =  (Schema<T>) elementClass.newInstance().cachedSchema();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("element schema is " + schema);
		return schema;
	}
}
