package com.simple.engine.service.hadoop.mrv1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.AnalyticsOperationOutput;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.engine.service.r.REXPProtos;
import com.simple.engine.service.r.REXPProtos.REXP;
import com.simple.original.api.exceptions.RAnalyticsException;
import com.simple.radapter.AdapterFactory;
import com.simple.radapter.api.IRAdapter;
import com.twitter.elephantbird.mapreduce.io.ProtobufBlockWriter;
import com.twitter.elephantbird.mapreduce.io.ProtobufWritable;

public class ROperationMapper extends
		Mapper<Text, Text, Text, ProtobufWritable<REXPProtos.REXP>> {

	private static final Logger logger = Logger
			.getLogger(ROperationMapper.class.getName());

	private ProtobufWritable<REXPProtos.REXP> protoWritable = ProtobufWritable
			.newInstance(REXPProtos.REXP.class);

	private IRAdapter rAdapter = null;

	public ROperationMapper() {

	}

	public void run(Context context) throws IOException, InterruptedException {
		rAdapter = AdapterFactory.createAdapter();
		rAdapter.connect();

		Configuration configuration = context.getConfiguration();
		OperationConfig opConfig = new OperationConfig(configuration);

		AnalyticsOperation operation = null;
		try {
			operation = opConfig.getOperation();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		RAnalyticsOperation rOperation = (RAnalyticsOperation) operation;

		logger.finest("Calling wait for engine");

		logger.finest("Assigning code to operation " + rOperation.getCode());
		rAdapter.


		ProtobufBlockWriter<REXPProtos.REXP> protobufBlockWriter = new ProtobufBlockWriter<REXPProtos.REXP>(
				new FileOutputStream("/tmp/test_output"), REXPProtos.REXP.class);

		for (AnalyticsOperationOutput output : rOperation.getOutputs()) {

			logger.info("Fetching output " + output.getName());
			REXP rexp = engine.eval(output.getName());

			if (rexp == null) {
				logger.info("Workspace object not found with value for rexp"
						+ output.getName());
				return;
			}

			logger.info("Found rexp " + rexp.toString());

			logger.info("TYpe is " + rexp.getType());
			if (rexp.getType() == REXP.STRSXP) {
				String[] strings = rexp.asStringArray();
				System.out.println("\n\n\n\n\n\n DONIG OUTPUT");
				
				for (String s : strings) {
					
					logger.info("WRiting to ocntext");
					protobufBlockWriter.write(REXPProtos.REXP
							.newBuilder()
							.addStringValue(
									REXPProtos.STRING.newBuilder().setStrval(s)
											.build()).build());
				}
			}
		}
		protobufBlockWriter.close();
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
	private static REXP getMetricPlotFromWorkspace(Rengine engine,
			String plotVaribleName) throws RAnalyticsException {
		return engine.eval("paste(readBin(\"" + plotVaribleName
				+ "\", what=\"raw\", n=1e6), collapse=\"\")");
		// return engine.eval("readBin(\"" + plotVaribleName +
		// "\", \"what=raw\", n=999999)");
	}

	private static REXP getMetricFromWorkspace(Rengine engine, String variable) {
		return engine.eval(variable);
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

	/**
	 * Called once at the end of the task.
	 * 
	 * Shut down the engine.
	 */
	protected void cleanup(Context context) throws IOException,
			InterruptedException {
		debugWorkspace(engine);
		engine.rniStop(0);
		engine.end();
	}

}
