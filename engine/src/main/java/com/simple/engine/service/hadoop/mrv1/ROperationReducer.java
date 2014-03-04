package com.simple.engine.service.hadoop.mrv1;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.simple.domain.model.AnalyticsOperationOutput;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.engine.metric.Metric;
import com.simple.original.api.analytics.IAnalyticsOperationOutput.Type;
import com.simple.original.api.analytics.IMetric;
import com.simple.radapter.RAdapterFactory;
import com.simple.radapter.api.IRAdapter;
import com.simple.radapter.api.RAdapterException;
import com.simple.radapter.protobuf.REXPProtos;
import com.simple.radapter.protobuf.REXPProtos.REXP;
import com.twitter.elephantbird.mapreduce.io.ProtobufWritable;

public class ROperationReducer extends Reducer<Text, ProtobufWritable<REXPProtos.REXP>, IMetricKey, IMetricWritable> implements
		Configurable {

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
	 * The configuration for the job.
	 */
	private Configuration conf;
	
	public ROperationReducer() {
	}



	@Override
	public synchronized void run(Context context) throws IOException, InterruptedException {
		assert (conf != null) : "configuration not specified";
		setup(context);

		OperationConfig configuration = new OperationConfig(conf);

		RAnalyticsOperation operation = null;

		try {
			operation = (RAnalyticsOperation) configuration.getOperation();
		} catch (ConfigurationException e) {
			throw new RuntimeException("cannot extract operation from configuration, cannot continue, see cause:", e);
		}

		if (operation == null) {
			throw new RuntimeException("Operation cannot be null");
		}

		try {
			try {
				localAdapter.get().connect();
			} catch (RAdapterException e) {
				throw new IOException("unable to connect to R environment", e);
			}

			String code = operation.getCode();
			logger.fine("Assigning code to operation " + code);

			REXP output = localAdapter.get().exec(code);

			logger.info("Script result " + output.getRclass());

			writeOutputsToContext(operation.getOutputs(), context);

			cleanup(context);
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
					rexp = localAdapter.get().getPlot(output.getName());
				} else {
					rexp = localAdapter.get().get(output.getName());
				}

				if (rexp == null) {
					logger.log(Level.WARNING, "rexp not found => " + output.getName());
					continue;
				}

				logger.info("found rexp => type " + rexp.getRclass());
				
				Metric plot = new Metric(rexp);
				
				logger.info("rexp byte sized " + plot.serialize());
				
				context.write(new MetricKey(output.getName()), new MetricWritable<IMetric>(plot));

		//		logger.info("Output type is " + context.getOutputValueClass());
				
			} catch (RAdapterException e) {
				logger.log(Level.SEVERE, "Error while retrieving output => " + output.getName(), e);
			}
		}
	}

	public static Put resultToPut(ImmutableBytesWritable key, Result result) {
		Put put = new Put(key.get());

		return put;
	}

	/**
	 * Called once at the end of the task. This has to clean up the r
	 * environment so that it does not create a memory leak. This is most
	 * important for local jobs that are running inside the jvm other tasks not
	 * so much because they are reaped at the end of the job. Shut down the
	 * engine.
	 */
	protected void cleanup(Context context) throws IOException, InterruptedException {
		logger.info("caling close on reduer");
		if (localAdapter.get() != null) {
			localAdapter.get().disconnect();
		}

		localAdapter.remove();
	}

	@Override
	public void setConf(Configuration conf) {
		this.conf = conf;
	}

	@Override
	public Configuration getConf() {
		return conf;
	}
}
