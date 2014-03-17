package com.simple.engine.service.hadoop.mrv2;

import java.io.IOException;

import com.simple.engine.api.IMetricWritable;
import com.simple.engine.service.hadoop.AbstractMapper;
import com.simple.original.api.orchestrator.IMetricKey;

public class ROperationMapper extends AbstractMapper<IMetricKey, IMetricWritable, IMetricKey, IMetricWritable> {

	@Override
	protected void map(IMetricKey key, IMetricWritable value, Context context) throws IOException, InterruptedException {
		context.write(key, value);
	}
}