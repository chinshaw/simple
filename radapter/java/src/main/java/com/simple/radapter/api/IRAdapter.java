package com.simple.radapter.api;

public interface IRAdapter {

	public static final int GLOBAL_ENVIRONMENT = 0;
	
	
	/**
	 * Parse a string into an R expression
	 * @param command R command to execute
	 * @param parts Number of parts contained in the string
	 * @return 
	 */
	public long parse(String command, int parts);
	
	/**
	 * 
	 * @param exp R expression, this can be created from parse
	 * @param environment Environment to use, 0 for global environment
	 * @return
	 */
	public long eval(long exp, long environment);
	
	
	/**
	 * Used to assign an expression into the workspace, the expression needs to be 
	 * created using the parse function.
	 * @param name The environment variable to assign the expression to.
	 * @param exp The R expression created using the parse function.
	 * @param environment Environment to use.
	 * @return
	 */
	public boolean assign(String name, long exp, long environment);
	
	
	/**
	 * Get the type of the expression;
	 * @param expression
	 * @return
	 */
	public int getExpressionType(long expression);
	
	public String getString();

	public long setString(String string);
	
	public String[] getStrings();
	
	public long setStrings(String[] strings);
	
	public int[] getInts();
	
	public long setInts(int[] ints);
	
	public double[] getDoubles();
	
	public long setDoubles(double[] doubles);
	
	public long[] getVector();
	
	public boolean[] getBooleans();
	
	public long setBooleans(boolean[] booleans);	
}