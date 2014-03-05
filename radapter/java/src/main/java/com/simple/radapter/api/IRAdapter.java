package com.simple.radapter.api;

import java.io.File;

import com.simple.radapter.protobuf.REXP;

public interface IRAdapter {

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
	

    public REXP get(String var) throws RAdapterException;   
    
    public REXP set(String var, REXP rexp) throws RAdapterException;  
    
    public REXP exec(File file) throws RAdapterException;
	
	public REXP exec(String script) throws RAdapterException;

	public REXP getPlot(String string) throws RAdapterException;
	
}