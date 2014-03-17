package com.simple.engine.service.hadoop;

import org.apache.hadoop.mapreduce.Reducer;

import com.simple.engine.api.IMetricWritable;
import com.simple.original.api.orchestrator.IMetricKey;

public class AbstractReducer<KEYIN extends IMetricKey, VALUEIN extends IMetricWritable, KEYOUT extends IMetricKey, VALUEOUT extends IMetricWritable>
		extends Reducer<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {

}
