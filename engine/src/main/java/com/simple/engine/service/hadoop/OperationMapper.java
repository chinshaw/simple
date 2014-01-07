package com.simple.engine.service.hadoop;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.rosuda.JRI.Rengine;

import com.simple.domain.RAnalyticsOperation;
import com.simple.domain.metric.Metric;

public class OperationMapper extends Mapper<Text, Text, Text, Text> {
	
	
	private static final String args[] = { "vanilla" };

	private Rengine engine;

	public OperationMapper() {
		engine = new Rengine(args, false, new ConsoleIO());
	}

	public void run(Context context) throws IOException, InterruptedException {
		Configuration configuration = context.getConfiguration();
		String serializedOperation = configuration.get(JobUtils.R_OPERATION_PARAM);

		RAnalyticsOperation operation;
		try {
			operation = JobUtils.unSerializeObject(serializedOperation);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Unable to read operation paramter from job", e);
		}

		if (!engine.waitForR()) {
			throw new RuntimeException("Unable to connect to R");
		}

		HashMap<Long, Metric> metrics = new HashMap<Long, Metric>();

		String code = operation.getCode();
		
		engine.eval(code);

	}
}
