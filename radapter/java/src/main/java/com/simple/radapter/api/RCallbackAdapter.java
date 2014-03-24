package com.simple.radapter.api;

public interface RCallbackAdapter {

	
	public void writeStdOut(String message);
	
	
	public void writeStdErr(String message);

    public void flush();
}
