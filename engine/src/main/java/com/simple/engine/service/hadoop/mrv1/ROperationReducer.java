package com.simple.engine.service.hadoop.mrv1;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.simple.domain.model.AnalyticsOperationOutput;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.original.api.analytics.IAnalyticsOperationOutput.Type;
import com.simple.radapter.RAdapterFactory;
import com.simple.radapter.api.IRAdapter;
import com.simple.radapter.api.RAdapterException;
import com.simple.radapter.protobuf.REXPProtos;
import com.simple.radapter.protobuf.REXPProtos.REXP;
import com.twitter.elephantbird.mapreduce.io.ProtobufWritable;

public class ROperationReducer extends Reducer<Text, ProtobufWritable<REXPProtos.REXP>, Text, ProtobufWritable<REXPProtos.REXP>> implements Configurable {

	private static final Logger logger = Logger.getLogger(ROperationMapper.class.getName());

	/**
	 * This is the required way to access the r adapter. It must be thread local
	 * because it is not thread safe. This must also be cleaned up in the
	 * cleanup method so that it will not leak memory.
	 */
	private static final ThreadLocal<IRAdapter> localAdapter = new ThreadLocal<IRAdapter>() {
		protected IRAdapter initialValue() {
			return RAdapterFactory.createAdapter();
		}
	};

	/**
	 * Get the adapter for the session.
	 */
	private static final IRAdapter adapter = localAdapter.get();

	/**
	 * The configuration for the job.
	 */
	private Configuration _conf;

	public ROperationReducer() {
	}

	@Override
	public synchronized void run(Context context) throws IOException, InterruptedException {
		assert(_conf != null) : "configuration not specified";
		
		OperationConfig configuration = new OperationConfig(_conf);

		RAnalyticsOperation operation = null;

		try {
			operation = (RAnalyticsOperation) configuration.getOperation();
		} catch (ConfigurationException e) {
			throw new RuntimeException("cannot extract operation from configuration, cannot continue, see cause:" , e);
		}
		
		if (operation == null) {
			throw new RuntimeException("Operation cannot be null");
		}

		try {
			try {
				adapter.connect();
			} catch (RAdapterException e) {
				throw new IOException("unable to connect to R environment", e);
			}

			String code = operation.getCode();
			logger.info("Assigning code to operation " + code);

			REXP output = adapter.exec(code);
			logger.info("Script result " + output.getRclass());

			writeOutputsToContext(operation.getOutputs(), context);
		} catch (RAdapterException e) {
			e.printStackTrace();
		}
	}

	private void writeOutputsToContext(Collection<AnalyticsOperationOutput> outputs, Context context) throws IOException,
			InterruptedException {

		for (AnalyticsOperationOutput output : outputs) {
			logger.fine("fetching output from workspace => " + output.getName());
			try {

				REXP rexp = null;
				if (output.getOutputType() == Type.BINARY || output.getOutputType() == Type.GRAPHIC) {
					rexp = adapter.getPlot(output.getName());
				} else {
					rexp = adapter.get(output.getName());
				}

				if (rexp == null) {
					logger.log(Level.WARNING, "rexp not found => " + output.getName());
					continue;
				}

				logger.fine("found rexp => type " + rexp.getRclass());

				ProtobufWritable<REXP> protoWritable = ProtobufWritable.newInstance(REXP.class);
				protoWritable.set(rexp);
				context.write(new Text(output.getName()), protoWritable);

			} catch (RAdapterException e) {
				logger.log(Level.SEVERE, "Error while retrieving output => " + output.getName(), e);
			}
		}
	}

	/**
	 * Called once at the end of the task. This has to clean up the r
	 * environment so that it does not create a memory leak. This is most
	 * important for local jobs that are running inside the jvm other tasks not
	 * so much because they are reaped at the end of the job. Shut down the
	 * engine.
	 */
	protected void cleanup(Context context) throws IOException, InterruptedException {
		IRAdapter adapter = localAdapter.get();
		if (adapter != null) {
			adapter.disconnect();
		}

		localAdapter.remove();
	}

	@Override
	public void setConf(Configuration conf) {
		this._conf = conf;
	}

	@Override
	public Configuration getConf() {
		return _conf;
	}

}
