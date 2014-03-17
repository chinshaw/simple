package com.simple.engine.service.hadoop;

import org.apache.hadoop.mapreduce.Mapper;

import com.simple.engine.api.IMetricWritable;
import com.simple.original.api.orchestrator.IMetricKey;

public class AbstractMapper<KEYIN extends IMetricKey, VALUEIN extends IMetricWritable, KEYOUT extends IMetricKey, VALUEOUT extends IMetricWritable>
		extends Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {

}
