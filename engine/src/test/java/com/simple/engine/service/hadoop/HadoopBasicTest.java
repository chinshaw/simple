package com.simple.engine.service.hadoop;

import org.junit.Test;

import com.simple.domain.RAnalyticsOperation;
import com.simple.engine.service.AnalyticsOperationException;

public class HadoopBasicTest {

	private Executor executor = new Executor();
	
	@Test
	public void testBasic() throws AnalyticsOperationException {
		RAnalyticsOperation operation = new RAnalyticsOperation("runTestScript");
		operation.setCode("print ( \"Hello, world!\", quote = FALSE )");
		executor.execute(null, operation, null);
	}
}

