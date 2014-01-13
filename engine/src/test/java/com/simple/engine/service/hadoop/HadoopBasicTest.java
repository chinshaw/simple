package com.simple.engine.service.hadoop;

import java.io.IOException;

import org.junit.Test;

import com.simple.domain.AnalyticsOperationOutput;
import com.simple.domain.RAnalyticsOperation;
import com.simple.engine.service.AnalyticsOperationException;
import com.simple.engine.utils.ScriptUtils;
import com.simple.original.api.analytics.IAnalyticsOperationOutput.Type;

public class HadoopBasicTest {

	private Executor executor = new Executor();
	
	@Test
	public void testBasic() throws AnalyticsOperationException {
		RAnalyticsOperation operation = new RAnalyticsOperation("runTestScript");
		operation.setCode("print ( \"Hello, world!\", quote = FALSE )");
		executor.execute(null, operation, null);
	}
	
	@Test
	public void testGraphic() throws IOException, AnalyticsOperationException {
		String script = ScriptUtils.getScriptCode("/com/simple/engine/rscripts/BollingerScript.R");
		RAnalyticsOperation operation = new RAnalyticsOperation("runTestScript");
		operation.getOutputs().add(new AnalyticsOperationOutput("temp.png", Type.BINARY));
		//operation.addOutput(new AnalyticsOperationOutput("metricPlot",IAnalyticsOperationOutput.Type.GRAPHIC));
		operation.setCode(script);
		executor.execute(null, operation, null);
	}
}