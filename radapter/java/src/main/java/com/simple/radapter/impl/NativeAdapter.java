package com.simple.radapter.impl;

import com.simple.radapter.api.IRAdapter;

public class NativeAdapter implements IRAdapter {
	
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
	public String[] getStringArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getIntArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getDoubleArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long[] getVector() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean[] getBooleanArray() {
		// TODO Auto-generated method stub
		return null;
	}
}
