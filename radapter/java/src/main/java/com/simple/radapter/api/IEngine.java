package com.simple.radapter.api;

import com.simple.radapter.exceptions.RAdapterException;


public interface IEngine {

	public IRexp eval(String code) throws RAdapterException;
	
	public void assign(String variableName, IRexp rexp) throws RAdapterException;
}
