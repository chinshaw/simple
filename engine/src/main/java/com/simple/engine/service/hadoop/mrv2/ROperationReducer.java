package com.simple.engine.service.hadoop.mrv2;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Reducer;

import com.simple.domain.model.AnalyticsOperationOutput;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.engine.api.IMetric;
import com.simple.engine.api.IMetricKey;
import com.simple.engine.api.IMetric.MediaType;
import com.simple.engine.metric.MetricKey;
import com.simple.engine.metric.MetricString;
import com.simple.engine.metric.RexpUtils;
import com.simple.engine.service.hadoop.config.ConfigurationException;
import com.simple.engine.service.hadoop.config.OperationConfig;
import com.simple.engine.service.hadoop.io.IMetricWritable;
import com.simple.engine.service.hadoop.io.MetricWritable;
import com.simple.original.api.analytics.IAnalyticsOperationOutput.Type;
import com.simple.radapter.RAdapterFactory;
import com.simple.radapter.api.IRAdapter;
import com.simple.radapter.api.RAdapterException;
import com.simple.radapter.protobuf.REXP;

public class ROperationReducer extends
		Reducer<IMetricKey, IMetricWritable, IMetricKey, IMetricWritable>
		implements Configurable {

	private static final Logger logger = Logger
			.getLogger(ROperationMapper.class.getName());

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
	public synchronized void run(Context context) throws IOException,
			InterruptedException {
		assert (conf != null) : "configuration not specified";

		while (context.nextKey()) {
			IMetricKey key = context.getCurrentKey();
			Iterator<IMetricWritable> iter = context.getValues().iterator();
			
			while(iter.hasNext()) {
				IMetricWritable writable = iter.next();
				MetricString string = (MetricString) writable.getMetric();
				logger.info(" Key is " + key.toString() + " Value is " + string.getStringValue());
			}
		}

		try {
			setup(context);

			// Wrap the context.
			RAnalyticsOperation operation = (RAnalyticsOperation) OperationConfig
					.getOperation(getConf());

			if (operation == null) {
				throw new RuntimeException("Operation cannot be null");
			}

			try {
				try {
					localAdapter.get().connect();
				} catch (RAdapterException e) {
					throw new IOException("unable to connect to R environment",
							e);
				}

				String code = operation.getCode();
				logger.fine("Assigning code to operation " + code);

				// executing script
				localAdapter.get().exec(code);
				// write the outputs to the context
				writeOutputsToContext(operation.getOutputs(), context);
			} catch (RAdapterException e) {
				e.printStackTrace();
			}
		} catch (ConfigurationException e) {
			throw new RuntimeException(
					"cannot extract operation from configuration, unbale to continue, see cause:",
					e);
		} finally {
			cleanup(context);
		}
	}

	private void writeOutputsToContext(
			Collection<AnalyticsOperationOutput> outputs, Context context)
			throws IOException, InterruptedException {

		for (AnalyticsOperationOutput output : outputs) {
			logger.fine("fetching output from workspace => " + output.getName());
			try {

				REXP rexp = null;
				if (output.getOutputType() == Type.BINARY
						|| output.getOutputType() == Type.GRAPHIC) {
					rexp = localAdapter.get().getPlot(output.getName());
				} else {
					rexp = localAdapter.get().get(output.getName());
				}

				if (rexp == null) {
					logger.log(Level.WARNING,
							"rexp not found => " + output.getName());
					continue;
				}

				logger.info("found rexp => type " + rexp.getRclass());

				IMetric<?> metric = RexpUtils.toMetric(rexp);

				context.write(new MetricKey("1"),
						new MetricWritable<IMetric<?>>(metric, MediaType.APPLICATION_PROTOBUF));
				
			} catch (RAdapterException e) {
				logger.log(Level.SEVERE, "Error while retrieving output => "
						+ output.getName(), e);
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
	protected void cleanup(Context context) throws IOException,
			InterruptedException {
		logger.info("caling close on reduer");
		if (localAdapter.get() != null) {
			localAdapter.get().disconnect();
		}

		localAdapter.remove();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setConf(Configuration conf) {
		this.conf = conf;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getConf() {
		return conf;
	}
}
