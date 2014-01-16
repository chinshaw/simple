package com.simple.radapter;

import com.simple.radapter.api.IRAdapter;

public class NativeAdapter implements IRAdapter {
	
	static {
		try {
			System.loadLibrary("radapter");
		} catch (UnsatisfiedLinkError e) {
			String searchPath = System.getProperty("java.library.path");
			throw new RuntimeException("Unable to find native library in the following paths " + searchPath, e);
		}
	}
	
	public static final int GLOBAL_ENVIRONMENT = 0;

	
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
	public synchronized native String getString();

	@Override
	public synchronized native long setString(String string);

	@Override
	public synchronized native String[] getStrings();

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
