package com.artisan.orchestrator.hadoop.job.reducer;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.Iterator;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.artisan.orchestrator.hadoop.job.IOCJobInjector;
import com.artisan.orchestrator.hadoop.job.config.OperationConfiguration;
import com.artisan.orchestrator.hadoop.job.io.MetricWritable;
import com.artisan.orchestrator.hadoop.job.utils.RexpUtils;
import com.google.inject.Inject;
import com.simple.api.orchestrator.IMetricKey;
import com.simple.orchestrator.api.AbstractMetricReducer;
import com.simple.orchestrator.api.IMetricWritable;
import com.simple.orchestrator.api.IRHadoopOperationOutput.Type;
import com.simple.orchestrator.api.conf.ConfigurationException;
import com.simple.orchestrator.api.event.IEventConnector;
import com.simple.orchestrator.api.event.OperationReducerStateChange;
import com.simple.orchestrator.api.event.OperationReducerStateChange.State;
import com.simple.orchestrator.api.exception.ReducerException;
import com.simple.orchestrator.api.hadoop.operation.ROperation;
import com.simple.orchestrator.api.hadoop.operation.ROperationOutput;
import com.simple.orchestrator.api.metric.Metric;
import com.simple.orchestrator.api.metric.MetricString;
import com.simple.orchestrator.api.metric.OperationOutputKey;
import com.simple.orchestrator.api.rest.MediaType;
import com.simple.radapter.RAdapterFactory;
import com.simple.radapter.api.IRAdapter;
import com.simple.radapter.api.RAdapterException;
import com.simple.radapter.protobuf.REXPProtos.Rexp;

public class ROperationReducer extends AbstractMetricReducer<IMetricKey, IMetricWritable, IMetricKey, IMetricWritable>
		implements Configurable {

	private static final Logger logger = Logger.getLogger(ROperationReducer.class);

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

	private ROperation operation;

	private IEventConnector eventConnector;

	public ROperationReducer() {
	}

	/**
	 * Called once at the beginning of the task.
	 */
	protected void setup(Context context) throws IOException, InterruptedException {

		logger.info("VM ID " + ManagementFactory.getRuntimeMXBean().getName());
		logger.log(Level.ERROR, "RUNNING SETUP");
		IOCJobInjector.getInjector().injectMembers(this);
	}

	@Override
	public synchronized void run(Context context) throws IOException, InterruptedException {
		assert (conf != null) : "configuration not specified";
		setup(context);

		logger.info("Done with setup");
		try {

			logger.info("RUNNING");
			// Wrap the context.
			operation = (ROperation) OperationConfiguration.getOperation(getConf());
			if (operation == null) {
				throw new ReducerException("Operation cannot be null");
			}

			if (operation.getId() == null) {
				throw new ReducerException("Invalid id for operation => " + operation.getId());
			}

			logger.info("Calling do write workspace");
			// Write input to workspace
			doWriteToWorkspace(context);

			logger.info("Calling do operatoin");
			// Execute the operation
			doOperation(context);

			// write the outputs to the context
			doWriteOutputsToContext(operation.getOutputs(), context);
		} catch (ConfigurationException e) {
			throw new RuntimeException("cannot extract operation from configuration, unbale to continue, see cause:", e);
		} finally {
			cleanup(context);
		}
	}

	private void doWriteToWorkspace(Context context) throws IOException, InterruptedException {
		while (context.nextKey()) {
			IMetricKey key = context.getCurrentKey();
			Iterator<IMetricWritable> iter = context.getValues().iterator();

			while (iter.hasNext()) {
				IMetricWritable writable = iter.next();
				MetricString string = (MetricString) writable.getMetric();
				// logger.info(" Key is " + key.toString() + " Value is " +
				// string.getStringValue());

				// TODO assign these into the workspace.
			}
		}
	}

	private void doOperation(Context context) throws IOException, InterruptedException {
		logger.info("Calling doOperation ");
		try {
			try {
				localAdapter.get().connect();
			} catch (RAdapterException e) {
				throw new IOException("unable to connect to R environment", e);
			}

			String code = operation.getCode();
			logger.debug("Assigning code to operation " + code);

			logger.info("About to execute code");
			// executing script
			logger.info("Code is " + code);
			localAdapter.get().exec(code);

			logger.info("Done executing code");

		} catch (RAdapterException e) {
			throw new ReducerException("Failed to execute operation ", e);
		}
	}

	/**
	 * Write the outputs to the Hadoop
	 * {@link org.apache.hadoop.mapreduce.Mapper.Context}
	 * 
	 * @param outputs
	 * @param context
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void doWriteOutputsToContext(Collection<ROperationOutput> outputs, Context context) throws IOException,
			InterruptedException {

		if (outputs.size() < 1) {
			logger.warn("No outputs were configured for the operation so no metrics will be saved");
			return;
		}

		for (ROperationOutput output : outputs) {
			if (output.getId() == null) {
				logger.warn("skipping output invalid id for metric " + output.getWorkspaceVarName());
				continue;
			}
			logger.debug("fetching output from workspace => " + output.getWorkspaceVarName());
			try {
				
				logger.info("Type is " + output.getOutputType());
				Rexp rexp = null;
				if (output.getOutputType() == Type.BINARY || output.getOutputType() == Type.GRAPHIC) {
					logger.info("Calling get plot for plot named " + output.getWorkspaceVarName());
					rexp = localAdapter.get().getPlot(output.getWorkspaceVarName());
				} else {
					logger.info("Calling get output " + output.getWorkspaceVarName());
					rexp = localAdapter.get().get(output.getWorkspaceVarName());
				}
				

				if (rexp == null) {
					logger.warn("rexp not found => " + output.getWorkspaceVarName());
					continue;
				}

				logger.info("found rexp => type " + rexp.getRclass());
				OperationOutputKey key = new OperationOutputKey(operation.getId(), output.getId());

				Metric<?> metric = RexpUtils.toMetric(key, rexp);

				logger.info("Writing metric key => " + metric.getKey());
				context.write(metric.getKey(), new MetricWritable<Metric<?>>(metric, MediaType.APPLICATION_PROTOBUF));

			} catch (RAdapterException e) {
				logger.log(Level.ERROR, "Error while retrieving output => " + output.getWorkspaceVarName(), e);
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
		logger.debug(ROperationReducer.class.getName() + " -> cleanup()");
		if (localAdapter.get() != null) {
			localAdapter.get().disconnect();
		}

		localAdapter.remove();
		eventConnector.post(new OperationReducerStateChange(State.CLEANUP_COMPLETE));
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

	@Inject
	public void setEventConnector(IEventConnector eventConnector) {
		logger.info("Connector is " + eventConnector);
		this.eventConnector = eventConnector;
	}
}
