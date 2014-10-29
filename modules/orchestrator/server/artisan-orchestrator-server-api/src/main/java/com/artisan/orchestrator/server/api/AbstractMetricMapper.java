package com.artisan.orchestrator.server.api;

import org.apache.hadoop.mapreduce.Mapper;

import com.simple.api.orchestrator.IMetricKey;

public abstract class AbstractMetricMapper<KEYIN extends IMetricKey, VALUEIN extends IMetricWritable, KEYOUT extends IMetricKey, VALUEOUT extends IMetricWritable>
		extends Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {

}
