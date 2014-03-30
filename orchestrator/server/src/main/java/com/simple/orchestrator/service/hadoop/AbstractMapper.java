package com.simple.orchestrator.service.hadoop;

import org.apache.hadoop.mapreduce.Mapper;

import com.simple.api.orchestrator.IMetricKey;
import com.simple.orchestrator.api.IMetricWritable;

public class AbstractMapper<KEYIN extends IMetricKey, VALUEIN extends IMetricWritable, KEYOUT extends IMetricKey, VALUEOUT extends IMetricWritable>
		extends Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {

}
