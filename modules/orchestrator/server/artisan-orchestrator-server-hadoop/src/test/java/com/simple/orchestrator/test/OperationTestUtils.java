package com.simple.orchestrator.test;

import java.io.IOException;

import com.artisan.utils.ClasspathUtils;
import com.simple.orchestrator.api.hadoop.operation.ROperation;
import com.simple.orchestrator.api.hadoop.operation.ROperationOutput;

public class OperationTestUtils {

	
	public static final ROperation createTestOperation() throws IOException {
		String script = ClasspathUtils.getScriptCode("/com/simple/engine/rscripts/BollingerScript.R");
		ROperation operation = new ROperation("runTestScript");
		operation.setId(99999l);
		ROperationOutput out = new ROperationOutput("/tmp/instrument.png", ROperationOutput.Type.BINARY);
		out.setId(123l);
		operation.addOutput(out);
		//operation.addOutput(new AnalyticsOperationOutput("y", Type.TEXT));
		operation.setCode(script);
		
		return operation;
	}
}
