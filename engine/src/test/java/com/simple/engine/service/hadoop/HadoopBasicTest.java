package com.simple.engine.service.hadoop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;

import com.simple.domain.model.AnalyticsOperationOutput;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.dataprovider.HttpDataProvider;
import com.simple.engine.service.AnalyticsOperationException;
import com.simple.engine.service.hadoop.mrv1.OperationExecutor;
import com.simple.engine.utils.ScriptUtils;
import com.simple.original.api.analytics.IAnalyticsOperationOutput.Type;

public class HadoopBasicTest {

	private static final Logger logger = Logger.getLogger(HadoopBasicTest.class.getName());
	
	private OperationExecutor executor = new OperationExecutor();
	
	@Test
	public void testBasic() throws AnalyticsOperationException {
		RAnalyticsOperation operation = new RAnalyticsOperation("runTestScript");
		operation.setCode("print ( \"Hello, world!\", quote = FALSE )");
		executor.execute(null, operation, null);
	}
	
	@Test
	public void testGraphic() throws IOException, AnalyticsOperationException {
		logger.info("testGraphic");
		String script = ScriptUtils.getScriptCode("/com/simple/engine/rscripts/BollingerScript.R");
		RAnalyticsOperation operation = new RAnalyticsOperation("runTestScript");
		operation.getOutputs().add(new AnalyticsOperationOutput("bollinger.png", Type.BINARY));
		operation.setCode(script);
		HttpDataProvider dp = new HttpDataProvider("http://ichart.finance.yahoo.com/table.csv?s=HPQ&a=00&b=12&c=2013&d=00&e=15&f=2014&g=d&ignore=.csv");
		List<DataProvider> dps = new ArrayList<DataProvider>();
		dps.add(dp);
		
		logger.info("Doing execute");
		executor.execute(null, operation, dps);
	}
}
