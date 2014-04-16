package com.simple.radapter.api;

import java.io.File;

import com.simple.radapter.protobuf.REXPProtos.Rexp;

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
	

    public Rexp get(String var) throws RAdapterException;   
    
    public Rexp set(String var, Rexp rexp) throws RAdapterException;  
    
    public Rexp exec(File file) throws RAdapterException;
	
	public Rexp exec(String script) throws RAdapterException;

	public Rexp getPlot(String string) throws RAdapterException;
	
}