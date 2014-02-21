package com.simple.engine.service.hadoop.mrv1;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.simple.domain.model.AnalyticsOperationOutput;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.original.api.exceptions.RAnalyticsException;
import com.simple.radapter.AdapterFactory;
import com.simple.radapter.api.IRAdapter;
import com.simple.radapter.api.RAdapterException;
import com.simple.radapter.protobuf.REXPProtos.REXP;

public class ROperationMapper extends
		Mapper<Text, Text, Text, REXP> {

	private static final Logger logger = Logger
			.getLogger(ROperationMapper.class.getName());

	private IRAdapter adapter = null;

	public ROperationMapper() {

	}

	public void run(Context context) throws IOException, InterruptedException {
		adapter = AdapterFactory.createAdapter();
		
		try {
			adapter.connect();
		} catch (RAdapterException e) {
			throw new RuntimeException("Unable to connect to R environment");
		}

		Configuration configuration = context.getConfiguration();
		OperationConfig opConfig = new OperationConfig(configuration);

		RAnalyticsOperation operation = null;
		try {
			operation = (RAnalyticsOperation) opConfig.getOperation();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		String code = operation.getCode();
		logger.finest("Assigning code to operation " + code);
		
		try {
			adapter.exec(code);
		} catch (RAdapterException e) {
			e.printStackTrace();
		}

		for (AnalyticsOperationOutput output : operation.getOutputs()) {
			logger.info("Fetching output " + output.getName());
			REXP rexp = null;
			try {
				rexp = adapter.get(output.getName());
			} catch (RAdapterException e) {
				e.printStackTrace();
			}

			if (rexp == null) {
				logger.info("Workspace object not found with value for rexp"
						+ output.getName());
				return;
			}

			logger.info("Found rexp " + rexp.toString());
			context.write(new Text(output.getName()), rexp);
			/*
			if (rexp instanceof IRexpString) {
				logger.info("WRiting to ocntext");
				protobufBlockWriter.write(REXPProtos.REXP
						.newBuilder().setRclass(RClass.STRING)
						.addStringValue(
								REXPProtos.STRING.newBuilder().setStrval(((IRexpString)rexp).getValue())
										.build()).build());
			
			}
			*/
		}
		//adapter.disconnect();
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

	private static REXP getMetricPlotFromWorkspace(Rengine engine,
			String plotVaribleName) throws RAnalyticsException {
		return engine.eval("paste(readBin(\"" + plotVaribleName
				+ "\", what=\"raw\", n=1e6), collapse=\"\")");
		// return engine.eval("readBin(\"" + plotVaribleName +
		// "\", \"what=raw\", n=999999)");
	}	 */

	private void debugEnvironment() {
		Map<String, String> environmentVariables = System.getenv();

		System.out.println("Environment Debug");
		for (Entry<String, String> entry : environmentVariables.entrySet()) {
			System.out.println(entry.getKey() + " -> " + entry.getValue());
		}
	}

	/**
	 * Called once at the end of the task.
	 * 
	 * Shut down the engine.
	 */
	protected void cleanup(Context context) throws IOException,
			InterruptedException {
	}
}
