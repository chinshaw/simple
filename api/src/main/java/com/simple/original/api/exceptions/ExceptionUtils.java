package com.simple.original.api.exceptions;

public class ExceptionUtils {

	/**
	 * This will take a throwable and convert it's stack trace to a string. It appends
	 * newline + tab + tab to every stack trace element. 
	 * 
	 * @param throwable
	 * @return
	 */
	public static String stackTraceToString(Throwable throwable) {
		StackTraceElement[] stack = throwable.getStackTrace();
		String exception = "";
		for (StackTraceElement s : stack) {
			exception = exception + s.toString() + "\n\t\t";
		}
		return exception;
	}
}
