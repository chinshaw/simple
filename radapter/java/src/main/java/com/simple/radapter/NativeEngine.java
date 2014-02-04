package com.simple.radapter;

import com.simple.radapter.api.IEngine;
import com.simple.radapter.api.RAdapterException;

class NativeEngine implements IEngine {

	
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
	
	public NativeEngine() {
		this(DEFAULT_R_ARGS);
	}
	
	public NativeEngine(String[] rArgs) {
		this.rArgs = rArgs;
	}
	
	public static final int GLOBAL_ENVIRONMENT = 0;


	/**
	 * {@inheritDoc}
	 * 
	 * Calls setup on the r environment, which is required to be run before
	 * running any R commands.
	 */
	public void startSession() throws RAdapterException {
		int configured = jniSetupR(rArgs);
		if (configured != 0) {
			throw new RAdapterException("Unable to connect to the R environment, R error code " + configured);
		} 
	}
	

	@Override
	public void stopSession() {
		
	}
	
	@Override
	public long parse(String command, int parts) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long eval(long exp, long environment) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getExpressionType(long expression) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long setString(String string) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String[] getStrings(long expression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long setStrings(String[] strings) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] getInts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long setInts(int[] ints) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double[] getDoubles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long setDoubles(double[] doubles) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long[] getVector() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean[] getBooleans() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long setBooleans(boolean[] booleans) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	@Override
	public boolean assign(String name, long exp, long environment) {
		return jniAssign(name, exp, environment);
	}
	

	@Override
	public String getString(String name) throws RAdapterException {
		long val = getWorkspaceVar(name);
		String[] strs = jniGetStrings(val);
		return strs[0];
	}
	
	
	private long getWorkspaceVar(String name) {
		long parsed = jniParse(name, 1);
		long evaled = jniEval(parsed, GLOBAL_ENVIRONMENT);
		return evaled;
	}
	

	private synchronized native int jniSetupR(String[] rArgs);
	
	/**
	 * Parse a string into an R expression
	 * @param command R command to execute
	 * @param parts Number of parts contained in the string
	 * @return 
	 */
	public synchronized native long jniParse(String command, int parts);
	
	/**
	 * 
	 * @param exp R expression, this can be created from parse
	 * @param environment Environment to use, 0 for global environment
	 * @return
	 */
	public synchronized native long jniEval(long exp, long environment);
	
	/**
	 * Used to assign an expression into the workspace, the expression needs to be 
	 * created using the parse function.
	 * @param name The environment variable to assign the expression to.
	 * @param exp The R expression created using the parse function.
	 * @param environment Environment to use.
	 * @return
	 */
	public synchronized native boolean jniAssign(String name, long exp, long environment);

	public synchronized native int jniGetExpressionType(long expression);
	
	public synchronized native String[] jniGetStrings(long expression);
	
	public synchronized native String jniGetString(long expression);

	public synchronized native long jniSetString(String string);

	public synchronized native long jniSetStrings(String[] strings);

	public synchronized native int[] jniGetInts();

	public synchronized native long jniSetInts(int[] ints); 

	public synchronized native double[] jniGetDoubles();

	public synchronized native long jniSetDoubles(double[] doubles);

	public synchronized native long[] jniGetVector();

	public synchronized native boolean[] jniGetBooleans();

	public synchronized native long jniSetBooleans(boolean[] booleans);
}
