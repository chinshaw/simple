package com.simple.engine.service.r.jri;

import java.util.logging.Logger;

import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;


public class ConsoleIO implements RMainLoopCallbacks {

	private static final Logger logger = Logger.getLogger(ConsoleIO.class.getName());
	
	@Override
	public void rBusy(Rengine arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public String rChooseFile(Rengine arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rFlushConsole(Rengine arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void rLoadHistory(Rengine arg0, String arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public String rReadConsole(Rengine arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rSaveHistory(Rengine arg0, String arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void rShowMessage(Rengine arg0, String message) {
		System.out.println("Messgae " + message);
		logger.info(message);
	}

	@Override
	public void rWriteConsole(Rengine arg0, String message, int arg2) {
		System.out.println("Message " + message);
		logger.info(message);
	}
}