package com.artisan.orchestrator.hadoop.job.reducer;

import com.artisan.orchestrator.server.api.AbstractMetricReducer;
import com.artisan.orchestrator.server.api.IMetricWritable;
import com.simple.api.orchestrator.IMetricKey;

/**
 * Null Reducer can be used for testing. It does nothing at all.
 * 
 * @author chris
 * 
 */
public class NullOperationReducer extends AbstractMetricReducer<IMetricKey, IMetricWritable, IMetricKey, IMetricWritable> {

}
