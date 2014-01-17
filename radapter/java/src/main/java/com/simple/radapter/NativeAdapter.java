package com.simple.radapter;

import com.simple.radapter.api.IRAdapter;
import com.simple.radapter.api.IRexp;
import com.simple.radapter.api.IRexpString;
import com.simple.radapter.exceptions.RAdapterException;

public class NativeAdapter implements IRAdapter {
	
	public static final String[] DEFAULT_R_ARGS = {"--vanilla"};
	
	static {
		try {
			System.loadLibrary("radapter");
		} catch (UnsatisfiedLinkError e) {
			String searchPath = System.getProperty("java.library.path");
			throw new RuntimeException("Unable to find native library in the following paths " + searchPath, e);
		}
	}
	
	private final String[] rArgs;
	
	public NativeAdapter() {
		this(DEFAULT_R_ARGS);
	}
	
	public NativeAdapter(String[] rArgs) {
		this.rArgs = rArgs;
	}
	
	public static final int GLOBAL_ENVIRONMENT = 0;


	/**
	 * {@inheritDoc}
	 * 
	 * Calls setup on the r environment, which is required to be run before
	 * running any R commands.
	 */
	public void connect() throws RAdapterException {
		int configured = setupR(rArgs);
		if (configured != 0) {
			throw new RAdapterException("Unable to connect to the R environment, R error code " + configured);
		} 
	}
	
	
	public synchronized native int setupR(String[] rArgs);
	
	/**
	 * Parse a string into an R expression
	 * @param command R command to execute
	 * @param parts Number of parts contained in the string
	 * @return 
	 */
	public synchronized native long parse(String command, int parts);
	
	/**
	 * 
	 * @param exp R expression, this can be created from parse
	 * @param environment Environment to use, 0 for global environment
	 * @return
	 */
	public synchronized native long eval(long exp, long environment);
	
	/**
	 * Used to assign an expression into the workspace, the expression needs to be 
	 * created using the parse function.
	 * @param name The environment variable to assign the expression to.
	 * @param exp The R expression created using the parse function.
	 * @param environment Environment to use.
	 * @return
	 */
	public synchronized native boolean assign(String name, long exp, long environment);

	@Override
	public synchronized native int getExpressionType(long expression);

	@Override
	public IRexpString getString(String name) throws RAdapterException {
		return getTypedRexp(name);
	}
	
	public <T extends IRexp<?>> T getTypedRexp(String name) throws RAdapterException {
		return (T) getRexp(name);
	}
	
	private IRexp<?> getRexp(String name) throws RAdapterException {
		long parsed = parse(name, 1);
		if (parsed != 0) {
			long evaled = eval(parsed, IRAdapter.GLOBAL_ENVIRONMENT);
			if (evaled == 0) {
				throw new RAdapterException("Unable to retrieve variable from workspace: " + name);
			}
			IRexp<?> rexp = RexpUtils.convert(this, evaled);
			return rexp;
		}
		throw new RAdapterException("Invalid rexp " + name);
	}
	
	public synchronized native String[] getStrings(long expression);
	
	public synchronized native String getString(long expression);

	@Override
	public synchronized native long setString(String string);

	@Override
	public synchronized native long setStrings(String[] strings);

	@Override
	public synchronized native int[] getInts();

	@Override
	public synchronized native long setInts(int[] ints); 

	@Override
	public synchronized native double[] getDoubles();

	@Override
	public synchronized native long setDoubles(double[] doubles);

	@Override
	public synchronized native long[] getVector();

	@Override
	public synchronized native boolean[] getBooleans();

	@Override
	public synchronized native long setBooleans(boolean[] booleans);
}
