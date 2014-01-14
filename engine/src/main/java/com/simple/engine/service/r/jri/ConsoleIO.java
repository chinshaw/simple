package com.simple.engine.service.r.jri;

import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;


public class ConsoleIO implements RMainLoopCallbacks {

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
	public void rShowMessage(Rengine arg0, String arg1) {
		System.out.println(arg1);
	}

	@Override
	public void rWriteConsole(Rengine arg0, String arg1, int arg2) {
		System.out.println(arg1);
	}
}