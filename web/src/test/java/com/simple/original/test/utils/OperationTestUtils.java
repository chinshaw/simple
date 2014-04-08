package com.simple.original.test.utils;

import java.io.IOException;

import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.AnalyticsOperationOutput;
import com.simple.domain.model.RAnalyticsOperation;

public class OperationTestUtils {

	
	public static final AnalyticsOperation createTestOperation() throws IOException {
		String script = ScriptUtils.getScriptCode("/com/simple/engine/rscripts/BollingerScript.R");
		RAnalyticsOperation operation = new RAnalyticsOperation("runTestScript");
		operation.addOutput(new AnalyticsOperationOutput("/tmp/instrument.png", AnalyticsOperationOutput.Type.BINARY));
		//operation.addOutput(new AnalyticsOperationOutput("y", Type.TEXT));
		operation.setCode(script);
		
		return operation;
	}
}
