package com.artisan.orchestrator.server.api;

import org.apache.hadoop.mapreduce.Reducer;

import com.simple.api.orchestrator.IMetricKey;

public class AbstractMetricReducer<KEYIN extends IMetricKey, VALUEIN extends IMetricWritable, KEYOUT extends IMetricKey, VALUEOUT extends IMetricWritable>
		extends Reducer<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {

}
