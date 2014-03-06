package com.simple.engine.service.hadoop.mrv1;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Mapper;

import com.simple.engine.metric.IMetricKey;
import com.simple.engine.service.hadoop.io.IMetricWritable;

public class ROperationMapper extends Mapper<IMetricKey, IMetricWritable, IMetricKey, IMetricWritable> {

	@Override
	protected void map(IMetricKey key, IMetricWritable value, Context context) throws IOException, InterruptedException {
		context.write(key, value);
	}
}