package com.simple.engine.service.r.rserve;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.ArrayUtils;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.AnalyticsOperationOutput;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.domain.model.SqlDataProvider;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.metric.Metric;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.domain.model.ui.ComplexInput;
import com.simple.domain.model.ui.DateInput;
import com.simple.domain.model.ui.StringInput;
import com.simple.engine.service.AnalyticsOperationException;
import com.simple.engine.service.AnalyticsTaskService;
import com.simple.engine.service.IAnalyticsOperationExecutor;
import com.simple.engine.service.r.MetricUtils;
import com.simple.original.api.analytics.IAnalyticsOperationInput;
import com.simple.original.api.analytics.IAnalyticsOperationOutput;
import com.simple.original.api.exceptions.RAnalyticsException;

public class RServeService implements IAnalyticsOperationExecutor {

	/**
	 * Our Operation logger logger.
	 */
	private static final Logger logger = AnalyticsTaskService.getLogger();

	/**
	 * Initialize this to false.
	 */
	private boolean operationSuccessful = false;

	/**
	 * This is our R service engine object.
	 */
	private final RConnection rConnection;

	@Inject
	public RServeService(@Named("rserve.host") String ipAddress)
			throws RserveException {
		rConnection = new RConnection(ipAddress);
	}

	/**
	 * Execute a task without any user inputs.
	 */
	@Override
	public void execute(String jobOwner, AnalyticsOperation operation,
			List<DataProvider> dataProvider) throws AnalyticsOperationException {
		execute(jobOwner, null, operation, dataProvider);
	}

	@Override
	public HashMap<Long, Metric> execute(String jobOwner,
			List<AnalyticsOperationInput> userInputs,
			AnalyticsOperation operation, List<DataProvider> dataProviders)
			throws AnalyticsOperationException {

		try {
			return _execute(userInputs, operation, dataProviders);
		} catch (RAnalyticsException e) {
			throw new AnalyticsOperationException(
					"Unable to execute operation " + operation.getName(), e);
		}
	}

	private synchronized HashMap<Long, Metric> _execute(
			List<AnalyticsOperationInput> userInputs,
			AnalyticsOperation operation, List<DataProvider> dataProviders)
			throws RAnalyticsException {

		if (!(operation instanceof RAnalyticsOperation)) {
			throw new RAnalyticsException(
					"Operation is not of type Rscript, not sure how you got here but this will need to be addressed before you can execute your script ");
		}

		/**
		 * List of the analytics task outputs.
		 */
		HashMap<Long, Metric> responseOutputs = null;

		logger.info("\n *** Beginning execution ***\n");
		RAnalyticsOperation rScriptOperation = (RAnalyticsOperation) operation;

		try {
			if (dataProviders != null && dataProviders.size() > 0) {
				assignDataProviders(dataProviders);
			}

			if (userInputs != null && userInputs.size() > 0) {
				assignUserInputs(userInputs);
			}

			String code = rScriptOperation.getCode();

			if (code == null || code.isEmpty()) {
				throw new RAnalyticsException(
						"R Script code cannot be null or empty");
			}

			logger.info("\n*** Executing Script ***\n");
			String stdout = executeCommand(code);
			logger.info(stdout);
			logger.info("\n*** Finished Script ***\n");

			// Everything looks good lets see what R gave us back.
			operationSuccessful = true;

			responseOutputs = retrieveOutputs(operation.getOutputs());

		} catch (REngineException e) {
			// We will try and get the error message from the server.
			try {
				REXPString errorMessage = (REXPString) rConnection
						.parseAndEval("geterrmessage()");
				String traceBack = getTraceBack(rConnection);
				String message = "\n\nUnable to execute analytics operation: '"
						+ operation.getName() + "'\n\nRServe Error: "
						+ e.getMessage() + "\n\nRserve local message: "
						+ errorMessage.asString() + "\n\nTraceBack: \n"
						+ traceBack;
				throw new RAnalyticsException(message, e);
			} catch (REngineException e1) {
				throw new RAnalyticsException(
						"Rengine exception occurred but we were unable to get cause from service ",
						e);
			} catch (REXPMismatchException e1) {
				throw new RAnalyticsException(
						"Rengine exception occurred but we were unable to get cause from service ",
						e);
			}
		} catch (REXPMismatchException e) {
			throw new RAnalyticsException("Mismatch while assigning value "
					+ e.getMessage(), e);
		} finally {
			// @TODO clear the R workspace.
			// Maybe close rconnection.
		}

		logger.info("Number of outputs found was " + responseOutputs.size());
		return responseOutputs;
	}

	/**
	 * This is used to handle assigning the data providers into the workspace.
	 * This is the first thing that is handled because the rest of the operation
	 * depends on data to run analyze. Right now this only handles
	 * {@linkplain SqlDataProvider} but the RDataProvider will be added soon.
	 * 
	 * @param dataProviders
	 * @throws REngineException
	 * @throws REXPMismatchException
	 */
	private void assignDataProviders(List<DataProvider> dataProviders)
			throws REngineException, REXPMismatchException {
		if (dataProviders == null) {
			throw new IllegalArgumentException(
					"Method assignDataProviders requires a valid list of dataProviders to assign");
		}
		throw new RuntimeException("TODO");
	}

	/**
	 * This will iterate of the list of inputs and assign those inputs into the
	 * R workspace before calling the script code. This way the operation will
	 * use these inputs as their user inputs. We have to handle
	 * {@link StringInput} which is essentially a string input,
	 * {@link DateInput} which is the date user input and the
	 * {@link ComplexInputWidget} which can contain sub inputs.
	 * 
	 * @param userInputs
	 *            List of all the {@link AnalyticsOperationInput} inputs.
	 * @throws REngineException
	 *             If there is a problem assigning the inputs into the
	 *             workspace.
	 * @throws REXPMismatchException
	 *             If we are assinging an input that does not match a specific
	 *             type.
	 */
	private void assignUserInputs(List<AnalyticsOperationInput> userInputs)
			throws REngineException, REXPMismatchException {
		if (userInputs == null) {
			throw new IllegalArgumentException(
					"Called assignUserInputs with invalid parameter "
							+ userInputs);
		}

		for (IAnalyticsOperationInput input : userInputs) {
			// Some checks for null
			if (input == null) {
				logger.info("\n*** Found a null input in the list of inptus ***\n");
				continue;
			}

			if (input.getInputName() == null) {
				logger.info("\n*** Found an input with no name ***\n");
				continue;
			}

			if (input instanceof StringInput) {
				String value = ((StringInput) input).getValue();
				if (value == null) {
					value = "";
				}
				// System.out.println("Assigning input " + input.getInputName()
				// + " value " + value);
				logger.info("\n*** Assiging input " + input.getInputName()
						+ " value \"" + value + "\"");
				rConnection.assign(input.getInputName(), value);
			} else if (input instanceof DateInput
					&& input.getValueAsString() != null) {
				rConnection.assign(input.getInputName(),
						input.getValueAsString());
			} else if (input instanceof ComplexInput) {

				// Handle the complex inputs by iterating over their sub inputs
				// and converting them to a dataframe.
				List<AnalyticsOperationInput> inputs = ((ComplexInput) input)
						.getInputs();

				RList inputsList = new RList();
				for (int i = 0; i < inputs.size(); i++) {
					AnalyticsOperationInput subInput = inputs.get(i);
					String value = subInput.getValueAsString();
					if (value == null || value.isEmpty()) {
						value = null;
					}

					REXPString strings = (REXPString) inputsList.get(subInput
							.getInputName());
					if (strings == null) {
						strings = new REXPString(value);
					} else {
						String[] values = (String[]) ArrayUtils.add(
								strings.asStrings(), value);
						strings = new REXPString(values);
					}

					inputsList.put(subInput.getInputName(), strings);
				}
				// If the list actually had some values we will convert
				// it to a datframe and assign it into the workspace.
				if (!inputsList.isEmpty()) {
					REXP dataFrame = REXP.createDataFrame(inputsList);
					rConnection.assign(input.getInputName(), dataFrame);
				}
			}
		}
	}

	/**
	 * This will retrieve all outputs specified in the analytics operation and
	 * assign them to a HashMap<String, Metric> class so that the analytics
	 * service can retrieve them by name.
	 * 
	 * @param rScriptOperation
	 *            Should be considered unmodifiable
	 * @throws RAnalyticsException
	 * @throws REXPMismatchException
	 */
	private synchronized HashMap<Long, Metric> retrieveOutputs(
			List<? extends IAnalyticsOperationOutput> operationOutputs)
			throws RAnalyticsException, REXPMismatchException {
		HashMap<Long, Metric> outputs = new HashMap<Long, Metric>();

		for (IAnalyticsOperationOutput outputOrigin : operationOutputs) {
			try {
				REXP workspaceRexp = null;
				Metric metric = null;

				switch (outputOrigin.getOutputType()) {
				case GRAPHIC:
					logger.fine("Retrieving workspace variable "
							+ outputOrigin.getName());
					workspaceRexp = this
							.getMetricPlotFromWorkspace(outputOrigin.getName());
					break;
				default:
					workspaceRexp = rConnection.get(outputOrigin.getName(),
							null, true);
				}

				if (workspaceRexp == null) {
					if (outputOrigin.isRequired()) {
						logger.severe("Unable to retrieve \"REQUIRED\" workspace variable with name"
								+ outputOrigin.getName()
								+ ". This may turn into an exception"
								+ "in the future and shoudld be fixed before that time");
					}
					continue;
				}

				// Create the metric and set it's origin so that we can figure
				// out what output it came from.
				metric = MetricUtils.createMetric(outputOrigin.getName(),
						workspaceRexp);
				metric.setOrigin((AnalyticsOperationOutput) outputOrigin);

				outputs.put(outputOrigin.getId(), metric);

			} catch (REngineException e) {
				logger.severe("Metric "
						+ outputOrigin.getName()
						+ " was not found in R workspace, is it declared in your operation."
						+ e.getMessage());
			}
		}

		return outputs;
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
	private REXP getMetricPlotFromWorkspace(String plotVaribleName)
			throws RAnalyticsException {
		REXP rexpPlot = null;

		try {
			// Lets try the correct way first.
			rexpPlot = rConnection.get(plotVaribleName, null, true);
		} catch (REngineException e) {
			logger.log(Level.WARNING,
					"Unable to retrieve plot from R, expected to find R variable with name:  "
							+ plotVaribleName, e);
		}

		if (rexpPlot == null) {
			try {
				rexpPlot = rConnection.parseAndEval("readBin(\""
						+ plotVaribleName + "\", \"raw\", 999999)");
			} catch (REXPMismatchException e) {
				logger.log(Level.WARNING,
						"Unable to retrieve plot from R, expected to find R variable with name:  "
								+ plotVaribleName, e);
			} catch (REngineException e) {
				logger.log(Level.WARNING,
						"Unable to retrieve plot from R, expected to find R variable with name:  "
								+ plotVaribleName, e);
			}
		}

		return rexpPlot;
	}

	/**
	 * Returns boolean if the operation was successful, this is used but you
	 * should also depend on exceptions to be thrown for Runtime errors.
	 */
	@Override
	public boolean isOperationSuccessful() {
		return operationSuccessful;
	}

	/**
	 * This executes a command and returns the String output back to the caller.
	 * This method is wrapped in a R capture.output so that we can retrieve the
	 * output from R when complete.
	 * 
	 * @param code
	 * @return
	 * @throws REngineException
	 * @throws REXPMismatchException
	 */
	private String executeCommand(String code) throws REngineException,
			REXPMismatchException {
		// Call into R with out script code.
		rConnection.assign(".tmpCode.", code);

		// Get stdout from the script and send it to the log.
		REXP stdout = rConnection
				.parseAndEval("paste(capture.output(eval(parse(text=.tmpCode.))),collapse='\\n')");
		return stdout.asString();
	}

	/**
	 * Close the connection to R services.
	 * 
	 * @throws REXPMismatchException
	 * @throws REngineException
	 */
	public void close() {
		rConnection.close();
	}

	public void shutdown() throws RserveException {
		rConnection.shutdown();
	}

	private String getTraceBack(RConnection connection) {

		String trace = "";

		REXP traceBack;
		try {
			traceBack = connection.parseAndEval("traceback()");
			if (traceBack != null && !traceBack.isNull()) {

				RList list = traceBack.asList();
				for (Object rexp : list) {
					trace += ((REXPString) rexp).asString() + "\n";
				}
			}
		} catch (REngineException e) {
			e.printStackTrace();
		} catch (REXPMismatchException e) {
			e.printStackTrace();
		}

		return trace;
	}

	/**
	 * Returns the underlying connection to R. This is mostly used for testing.
	 * 
	 * @return
	 */
	public REngine getConnection() {
		return rConnection;
	}

	@Override
	public void reset() throws RAnalyticsException {
		try {
			rConnection.parseAndEval("rm(list = ls())");
		} catch (REngineException e) {
			throw new RAnalyticsException("Unable to reset R connection", e);
		} catch (REXPMismatchException e) {
			throw new RAnalyticsException("Unable to reset R connection", e);
		}
	}
}
