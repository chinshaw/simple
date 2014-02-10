package com.simple.radapter.api;

public interface INativeEngine {

	String getString(String name) throws RAdapterException;

	void setString(String var, String value);

	void setStrings(String var, String[] strings);

	void setInts(String var, int[] ints);

	void setDoubles(String var, double[] doubles);

	void setBooleans(String var, boolean[] booleans);
	
	int[] getInts(long exp);

	/**
	 * Get a list of doubles from the R workspace.
	 * @param exp
	 * @return
	 */
	double[] getDoubles(long exp);

	/**
	 * Get an array of booleans
	 * @return
	 */
	boolean[] getBooleans();

	/**
	 * Get a list of strings
	 * @param exp
	 * @return
	 */
	String[] getStrings(long exp);


	/**
	 * Get the type of the expression;
	 * 
	 * @param expression
	 * @return
	 */
	public int getExpressionType(long expression);
}
