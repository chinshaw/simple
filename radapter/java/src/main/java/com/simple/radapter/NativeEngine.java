package com.simple.radapter;

import com.simple.radapter.api.IEngine;
import com.simple.radapter.api.IRAdapter;
import com.simple.radapter.api.IRexp;
import com.simple.radapter.exceptions.RAdapterException;

public class NativeEngine implements IEngine {
	
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
	public void assign(String variableName, IRexp<?> rexp) throws RAdapterException {

	}

}
