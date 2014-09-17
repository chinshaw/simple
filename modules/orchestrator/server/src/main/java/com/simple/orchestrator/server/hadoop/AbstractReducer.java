package com.simple.orchestrator.server.hadoop;

import org.apache.hadoop.mapreduce.Reducer;

import com.simple.api.orchestrator.IMetricKey;
import com.simple.orchestrator.api.IMetricWritable;

public class AbstractReducer<KEYIN extends IMetricKey, VALUEIN extends IMetricWritable, KEYOUT extends IMetricKey, VALUEOUT extends IMetricWritable>
		extends Reducer<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {

}
