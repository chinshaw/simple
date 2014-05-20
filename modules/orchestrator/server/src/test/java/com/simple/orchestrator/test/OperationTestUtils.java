package com.simple.orchestrator.test;

import java.io.IOException;

import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.AnalyticsOperationOutput;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.orchestrator.utils.ScriptUtils;

public class OperationTestUtils {

	
	public static final AnalyticsOperation createTestOperation() throws IOException {
		String script = ScriptUtils.getScriptCode("/com/simple/engine/rscripts/BollingerScript.R");
		RAnalyticsOperation operation = new RAnalyticsOperation("runTestScript");
		operation.setId(99999l);
		operation.addOutput(new AnalyticsOperationOutput("/tmp/instrument.png", AnalyticsOperationOutput.Type.BINARY));
		//operation.addOutput(new AnalyticsOperationOutput("y", Type.TEXT));
		operation.setCode(script);
		
		return operation;
	}
}
