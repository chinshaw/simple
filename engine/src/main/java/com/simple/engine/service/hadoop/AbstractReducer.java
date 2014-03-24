package com.simple.engine.service.hadoop;

import org.apache.hadoop.mapreduce.Reducer;

import com.simple.api.orchestrator.IMetricKey;
import com.simple.engine.api.IMetricWritable;

public class AbstractReducer<KEYIN extends IMetricKey, VALUEIN extends IMetricWritable, KEYOUT extends IMetricKey, VALUEOUT extends IMetricWritable>
		extends Reducer<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {

}
