package com.simple.radapter;

import com.simple.radapter.api.IRAdapter;

public class RAdapterFactory {

	private static IRAdapter adapter = null;
	
	public static final IRAdapter createAdapter() {
		if (adapter == null) {
			adapter = new NativeAdapter();
		}
		return adapter;
	}
}
