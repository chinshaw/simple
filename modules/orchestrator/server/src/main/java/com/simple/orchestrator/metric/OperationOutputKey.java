package com.simple.orchestrator.metric;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * This extends the Metric Key and converts the value to and from long values
 * for the task and operation output that the metric came from.
 * @author chris
 *
 */
public class OperationOutputKey extends MetricKey {

	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Empty Constructor
	 */
	public OperationOutputKey() {
	}

	public OperationOutputKey(Long taskId, Long outputId) {
		setValue(taskId, outputId);
	}
	
	/**
	 * Get the task id, this will look at the value byte array and read the first part of it
	 * to grab the first long value.
	 * @return
	 */
	public Long getTaskId() {
		return Bytes.toLong(Bytes.head(getValue(), Bytes.SIZEOF_LONG));
	}
	
	/**
	 * Get the output Id.
	 * @return
	 */
	public Long getOutputId() 	{
		return Bytes.toLong(Bytes.tail(getValue(), Bytes.SIZEOF_LONG));
	}
	
	/**
	 * Helper method to create a key from the task id's long id and 
	 * the metric's long id.
	 * 
	 * @param taskId
	 * @param metricId
	 * @return
	 */
	public static OperationOutputKey create(Long taskId, Long metricId) {
		return create(Bytes.toBytes(taskId), Bytes.toBytes(metricId));
	}
	
	/**
	 * This will create a key using the task id byte buffer appended with
	 * a dash and then the operation output id byte buffer.
	 * 
	 * @param taskId
	 * @param opId
	 * @return Metric key with value of the taskId and metricId concatenated with a single '-' character.
	 */
	public static OperationOutputKey create(byte[] taskId, byte[] outputId) {
		return new OperationOutputKey(Bytes.toLong(taskId), Bytes.toLong(outputId));
	}
	
	private void setValue(Long taskId, Long outputId) {
		setValue(Bytes.add(Bytes.toBytes(taskId), Bytes.toBytes(VALUE_SEPARATOR), Bytes.toBytes(outputId)));
	}
}