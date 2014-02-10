package com.simple.radapter.api;

public interface IRCallback {

	
	/**
	 * Call back for when R is printing to console.
	 * 
	 * @param adapter
	 * @param text This is the text that was printed to output
	 * @param ioType Output type is either stdout or stderr (0 or 1) respectively
	 */
	public void rWriteConsole(IRAdapter adapter, String text, int ioType);
	
	
	public void rShowMessage(IRAdapter adapter, String message);
	
}
