package com.simple.radapter;

import com.simple.radapter.api.RCallbackAdapter;

public class SimpleCallbackAdapter implements RCallbackAdapter {

	@Override
	public void writeStdOut(String message) {
		System.out.println(message);
	}

	@Override
	public void writeStdErr(String message) {
		System.err.println(message);
	}
}
