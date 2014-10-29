package com.artisan.orchestrator.hadoop.job.mapper;

import java.io.IOException;
import java.util.logging.Logger;

import com.artisan.orchestrator.server.api.AbstractMetricMapper;
import com.artisan.orchestrator.server.api.IMetricWritable;
import com.simple.api.orchestrator.IMetricKey;

/**
 * This is the mapper implementation for R and right now it simply passes the
 * inputs from the dataprovider to the context for the reducer later on.
 * 
 * @author chinshaw
 *
 */
public class ROperationMapper extends AbstractMetricMapper<IMetricKey, IMetricWritable, IMetricKey, IMetricWritable> {

	private static final Logger logger = Logger.getLogger(ROperationMapper.class.getName());

	@Override
	protected void map(IMetricKey key, IMetricWritable value, Context context) throws IOException, InterruptedException {
		context.write(key, value);

		logger.info("Calling do mapper");

	}
}