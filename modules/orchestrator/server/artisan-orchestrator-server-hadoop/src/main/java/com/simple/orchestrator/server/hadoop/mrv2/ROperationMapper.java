package com.simple.orchestrator.server.hadoop.mrv2;

import java.io.IOException;

import com.simple.api.orchestrator.IMetricKey;
import com.simple.orchestrator.api.AbstractMetricMapper;
import com.simple.orchestrator.api.IMetricWritable;

/**
 * This is the mapper implementation for R and right now it simply passes the inputs
 * from the dataprovider to the context for the reducer later on.
 * @author chinshaw
 *
 */
public class ROperationMapper extends AbstractMetricMapper<IMetricKey, IMetricWritable, IMetricKey, IMetricWritable> {

	@Override
	protected void map(IMetricKey key, IMetricWritable value, Context context) 
			throws IOException, InterruptedException {
		context.write(key, value);
	}
}