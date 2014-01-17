package com.simple.engine.service.hadoop;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Mapper;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.engine.service.r.jri.ConsoleIO;
import com.simple.original.api.exceptions.RAnalyticsException;

public class ROperationMapper extends Mapper<Text, Text, Text, Text> {

	private static final Logger logger = Logger.getLogger(ROperationMapper.class.getName());

	private static final String args[] = { "--vanilla" };

	private Rengine engine;

	public ROperationMapper() {
		engine = new Rengine(args, true, new ConsoleIO());
	}

	public void run(Context context) throws IOException, InterruptedException {
		Configuration configuration = context.getConfiguration();
		OperationConfig opConfig = new OperationConfig(configuration);

		AnalyticsOperation operation = null;
		try {
			operation = opConfig.getOperation();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		try {
			Class<? extends InputFormat<?,?>> inFormatter = context.getInputFormatClass();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		RAnalyticsOperation rOperation = (RAnalyticsOperation) operation;

		if (!engine.waitForR()) {
			throw new RuntimeException("Unable to connect to R");
		}

		engine.assign(".tmpCode.", rOperation.getCode());

		// Get stdout from the script and send it to the log.
		
		REXP rexp = engine.eval("eval(parse(text=.tmpCode.))");

		try {
			REXP imageTest = getMetricPlotFromWorkspace(engine, "temp.png");
		} catch (RAnalyticsException e) {
			e.printStackTrace();
		}

		debugWorkspace(engine);
		engine.end();
	}

	/**
	 * This will try to fetch the plot from the workspace in one of two ways.
	 * 
	 * 1. It will first try the newer syntax to grab the MetricPlot object from
	 * the workspace and use that to fetch it's plot. This is the preferred way
	 * because it also offers the ability to add violations to the plot.
	 * 
	 * 2. It will try to read the actual graphic from the workspace and use
	 * that. If the plotVaraibleName is an actual graphic it grab this binary
	 * object.
	 * 
	 * 
	 * @param chartName
	 *            The name of the actual file to fetch.
	 * @throws RAnalyticsException
	 *             If there is a problem executing the binary fetch operation.
	 * @throws
	 */
	private static REXP getMetricPlotFromWorkspace(Rengine engine, String plotVaribleName) throws RAnalyticsException {
		return engine.eval("paste(readBin(\"" + plotVaribleName + "\", what=\"raw\", n=1e6), collapse=\"\")");
		//return engine.eval("readBin(\"" + plotVaribleName + "\", \"what=raw\", n=999999)");
	}

	private void debugWorkspace(Rengine engine) {
		REXP workspace = engine.eval("ls()");
		
		System.out.println(workspace.toString());
	}
	
	private void debugEnvironment() {
		Map<String, String> environmentVariables = System.getenv();

		System.out.println("Environment Debug");
		for (Entry<String, String> entry : environmentVariables.entrySet()) {
			System.out.println(entry.getKey() + " -> " + entry.getValue());
		}
	}
}
