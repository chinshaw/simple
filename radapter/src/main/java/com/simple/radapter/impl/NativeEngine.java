package com.simple.radapter.impl;

import com.simple.radapter.api.IEngine;
import com.simple.radapter.api.IRAdapter;
import com.simple.radapter.api.IRexp;
import com.simple.radapter.exceptions.RAdapterException;

public class NativeEngine implements IEngine {

	static {
		try {
			System.loadLibrary("radapter");
		} catch (UnsatisfiedLinkError e) {
			String searchPath = System.getProperty("jri.library.path");
			throw new RuntimeException("Unable to find native library in the following paths" + searchPath);
		}
	}

	private IRAdapter adapter;

	public NativeEngine() {

	}

	public NativeEngine(IRAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public IRexp<?> eval(String code) throws RAdapterException {
		long expression = adapter.parse(code, 1);

		if (expression == 0) {
			// TODO add cause from parse.
			throw new RAdapterException("Unable to parse code");
		}
		
		expression = adapter.eval(expression, IRAdapter.GLOBAL_ENVIRONMENT);
		
		return RexpUtils.convert(adapter, expression);
	}

	@Override
	public void assign(String variableName, IRexp rexp) throws RAdapterException {

	}

}
