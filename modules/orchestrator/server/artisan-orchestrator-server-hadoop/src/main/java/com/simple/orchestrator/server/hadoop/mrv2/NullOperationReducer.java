package com.simple.orchestrator.server.hadoop.mrv2;

import com.simple.api.orchestrator.IMetricKey;
import com.simple.orchestrator.api.AbstractMetricReducer;
import com.simple.orchestrator.api.IMetricWritable;

/**
 * Null Reducer can be used for testing. It does nothing at all.
 * 
 * @author chris
 * 
 */
public class NullOperationReducer extends AbstractMetricReducer<IMetricKey, IMetricWritable, IMetricKey, IMetricWritable> {

}
