package com.simple.radapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import com.simple.radapter.api.EvalException;
import com.simple.radapter.api.INativeEngine;
import com.simple.radapter.api.IRAdapter;
import com.simple.radapter.api.IRexp;
import com.simple.radapter.api.ParseException;
import com.simple.radapter.api.RAdapterException;
import com.simple.radapter.api.RCallbackAdapter;
import com.simple.radapter.api.RConstants;

public class NativeAdapter implements IRAdapter, INativeEngine {

	static {
		try {
			System.loadLibrary("radapter");
		} catch (UnsatisfiedLinkError e) {
			String searchPath = System.getProperty("java.library.path");
			throw new RuntimeException(
					"Unable to find native library in the following paths "
							+ searchPath, e);
		}
	}

	private final String[] rArgs;

	private RCallbackAdapter console;

	public NativeAdapter() {
		this(DEFAULT_R_ARGS);
	}

	public NativeAdapter(String[] rArgs) {
		this.rArgs = rArgs;
		console = new SimpleCallbackAdapter();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Calls setup on the r environment, which is required to be run before
	 * running any R commands.
	 */
	@Override
	public void connect() throws RAdapterException {
		int configured = initR(rArgs);
		if (configured != 0) {
			throw new RAdapterException(
					"Unable to connect to the R environment, R error code "
							+ configured);
		}
	}

	@Override
	public void disconnect() {
		endR(0);
	}
	

	@Override
	public IRexp<?> execScript(String script) throws RAdapterException {
		setString(".tmpCode.", script);
		return execCommand("eval(parse(text=.tmpCode.))");
	}

	@Override
	public IRexp<?> execScript(File file) throws RAdapterException {
		if (file == null) {
			throw new IllegalArgumentException("File cannot be null");
		}

		String scriptCode = null;

		FileReader reader = null;
		BufferedReader br = null;
		try {
			try {
				logger.fine("executing r script " + file.getPath());
				reader = new FileReader(file);
				br = new BufferedReader(reader);

				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					sb.append("\n");
					line = br.readLine();
				}
				scriptCode = sb.toString();

			} catch (Exception e) {
				if (e instanceof IOException) {
					throw new Exception("Unable to read the file "
							+ file.getAbsolutePath());
				}
			} finally {
				reader.close();
				br.close();
			}
		} catch (Exception e) {
			throw new RAdapterException("Unable to parse your script ", e);
		}

		return execScript(scriptCode);
	}

	@Override
	public IRexp<?> execCommand(String command) throws RAdapterException {
		long parsed = jniParse(command, 1);
		if (parsed == 0) {
			throw new ParseException("Unable to parse command");
		}
		long evaled = jniEval(parsed, GLOBAL_ENVIRONMENT);
		if (evaled == 0) {
			throw new EvalException("Unable to execute command");
		}
		return RexpUtils.convert(this, evaled);
	}

	

	/**
	 * Used to return a rexp object.
	 * @throws RAdapterException 
	 */
	@Override
	public IRexp<?> get(String var) throws RAdapterException {
		long workspaceVar = getWorkspaceVar(var);
		return RexpUtils.convert(this, workspaceVar);
	}
	
	/**
	 * 
	 * 
	 * 
	 * TODO why does this require me to get REALSXP and not INTSXP.
	 * I would have figured that it would return an integer sxp.
	 */
	public int getInt(String var) {
		long workspaceVar = getWorkspaceVar(var);
		try {
			int type = jniGetExpressionType(workspaceVar);
			if (type != RConstants.REALSXP) {
				throw new IllegalArgumentException("invalid type for " + var);
			}
			jniProtect(workspaceVar);
			double[] doubles = jniGetDoubles(workspaceVar);
			return (int) doubles[0];
		} finally {
			jniUnprotect(0);
		}
	}

	public void setInt(String var, int val) {
		long exp = jniSetInts(new int[val]);
		if (exp == 0) {
			throw new ParseException();
		}
		jniAssign(var, exp, GLOBAL_ENVIRONMENT);
	}

	public IRexp<?> getRexp(String name) throws RAdapterException {
		long var = jniParse(name, 1);
		return RexpUtils.convert(this, var);
	}

	public int getExpressionType(long expression) {
		return jniGetExpressionType(expression);
	}

	@Override
	public String getString(String var) throws RAdapterException {
		long workspaceVar = getWorkspaceVar(var);
		return jniGetString(workspaceVar);
	}

	@Override
	public void setString(String var, String value) {
		long exp = jniSetString(value);
		if (exp == 0) {
			throw new ParseException();
		}
		jniAssign(var, exp, GLOBAL_ENVIRONMENT);
	}

	public String[] getStrings(long expression) {
		return jniGetStrings(expression);
	}

	public void setStrings(String var, String[] strings) {
		long exp = jniSetStrings(strings);
		if (exp == 0) {
			throw new ParseException();
		}
		jniAssign(var, exp, GLOBAL_ENVIRONMENT);
	}

	public int[] getInts(long exp) {
		return jniGetInts(exp);
	}

	public void setInts(String var, int[] ints) {
		long exp = jniSetInts(ints);
		if (exp == 0) {
			throw new ParseException();
		}
		jniAssign(var, exp, GLOBAL_ENVIRONMENT);
	}

	@Override
	public double[] getDoubles(long exp) {
		return jniGetDoubles(exp);
	}

	@Override
	public void setDoubles(String var, double[] doubles) {
		long exp = jniSetDoubles(doubles);
		if (exp == 0) {
			throw new ParseException();
		}
		jniAssign(var, exp, GLOBAL_ENVIRONMENT);

	}

	@Override
	public boolean[] getBooleans() {
		return jniGetBooleans();
	}

	@Override
	public void setBooleans(String var, boolean[] booleans) {
		long exp = jniSetBooleans(booleans);
		if (exp == 0) {
			throw new ParseException();
		}
		jniAssign(var, exp, GLOBAL_ENVIRONMENT);
	}

	private long getWorkspaceVar(String var) {
		long parsed = jniParse(var, 1);
		if (parsed == 0) {
			throw new ParseException();
		}

		long evaled = jniEval(parsed, GLOBAL_ENVIRONMENT);
		if (evaled == 0) {
			throw new EvalException("No var named " + var);
		}

		return evaled;
	}

	private synchronized native int initR(String[] rArgs);

	
	private synchronized native void endR(int flag);
	
	/**
	 * Parse a string into an R expression
	 * 
	 * @param command
	 *            R command to execute
	 * @param parts
	 *            Number of parts contained in the string
	 * @return
	 */
	public synchronized native long jniParse(String command, int parts);

	/**
	 * 
	 * @param exp
	 *            R expression, this can be created from parse
	 * @param environment
	 *            Environment to use, 0 for global environment
	 * @return
	 */
	public synchronized native long jniEval(long exp, long environment);

	/**
	 * Used to assign an expression into the workspace, the expression needs to
	 * be created using the parse function.
	 * 
	 * @param name
	 *            The environment variable to assign the expression to.
	 * @param exp
	 *            The R expression created using the parse function.
	 * @param environment
	 *            Environment to use.
	 * @return
	 */
	public synchronized native boolean jniAssign(String name, long exp,
			long environment);

	public synchronized native int jniGetExpressionType(long expression);

	public synchronized native String[] jniGetStrings(long expression);

	public synchronized native String jniGetString(long expression);

	public synchronized native long jniSetString(String string);

	public synchronized native long jniSetStrings(String[] strings);

	public synchronized native int[] jniGetInts(long expression);

	public synchronized native long jniSetInts(int[] ints);

	public synchronized native double[] jniGetDoubles(long exp);

	public synchronized native long jniSetDoubles(double[] doubles);

	public synchronized native long[] jniGetVector(long exp);

	public synchronized native boolean[] jniGetBooleans();

	public synchronized native long jniSetBooleans(boolean[] booleans);

	public synchronized native void jniProtect(long exp);

	public synchronized native void jniUnprotect(int count);

	/**
	 * JRI: R_WriteConsole call-back from R
	 * 
	 * @param text
	 *            text to disply
	 */
	public void jriWriteConsole(String text, int outputType) {
		if (outputType == 0) { // stdout
			console.writeStdOut(text);
		} else {
			console.writeStdErr(text);
		}
	}

	/**
	 * JRI: R_Busy call-back from R
	 * 
	 * @param which
	 *            state
	 */
	public void jriBusy(int which) {
	}

	/**
	 * JRI: R_ReadConsole call-back from R.
	 * 
	 * @param prompt
	 *            prompt to display before waiting for the input.<br>
	 *            <i>Note:</i> implementations should block for input. Returning
	 *            immediately is usually a bad idea, because the loop will be
	 *            cycling.
	 * @param addToHistory
	 *            flag specifying whether the entered contents should be added
	 *            to history
	 * @return content entered by the user. Returning <code>null</code>
	 *         corresponds to an EOF and usually causes R to exit (as in
	 *         <code>q()</doce>).
	 */
	public String jriReadConsole(String prompt, int addToHistory) {

		// System.out.println("Rengine.jreReadConsole BEGIN "
		// + Thread.currentThread());
		return null;
	}

	/**
	 * JRI: R_ShowMessage call-back from R
	 * 
	 * @param message
	 *            message
	 */
	public void jriShowMessage(String message) {
	}

	/**
	 * JRI: R_savehistory call-back from R
	 * 
	 * @param filename
	 *            name of the history file
	 */
	public void jriSaveHistory(String filename) {
	}

	// Member variables

	private static final Logger logger = Logger.getLogger(NativeAdapter.class
			.getName());

	public static final int GLOBAL_ENVIRONMENT = 0;

	/**
	 * Is it started, R is single threaded
	 */
	private static boolean started = false;

	public static final String[] DEFAULT_R_ARGS = { "--vanilla" };


}
