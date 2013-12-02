package com.simple.original.server.servlet;

import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.simple.original.api.exceptions.ExceptionUtils;

public class SimpleExceptionHandler implements ExceptionHandler {

	@Override
	public ServerFailure createServerFailure(Throwable throwable) {
		throwable.printStackTrace();

		return new ServerFailure("Server Error: "
				+ (throwable == null ? null : throwable.getMessage()),
				throwable.getClass().getName(), ExceptionUtils.stackTraceToString(throwable), true);
	}


}
