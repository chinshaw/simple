package com.simple.radapter.api;

import java.io.File;

public interface IRAdapter {

	public static final int GLOBAL_ENVIRONMENT = 0;

	/**
	 * Connect is used to connect to R, this must be called before any other
	 * operations may be called. Depending on the engine being used it may be
	 * necessary to configure the engine with it's parameters prior to
	 * connecting.
	 * 
	 * @throws RAdapterException
	 */
	public void connect() throws RAdapterException;

	/**
	 * Called to disconnect from the R environment and clean up the workspace.
	 * This will call the engines stop.
	 */
	public void disconnect();

	public IRexp<?> exec(String command) throws RAdapterException;

	public IRexp<?> execScript(String script) throws RAdapterException;

	public IRexp<?> execScript(File file) throws RAdapterException;
	
	/**
	 * This will assign a string into the workspace.
	 * 
	 * @param var
	 * @param value
	 */
	public void setString(String var, String value) throws RAdapterException;;

	public String getString(String var) throws RAdapterException;

	
}