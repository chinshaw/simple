package com.simple.radapter;

import com.simple.radapter.api.INativeEngine;
import com.simple.radapter.api.IRexp;
import com.simple.radapter.api.IRexpCollection;
import com.simple.radapter.api.IRexpString;
import com.simple.radapter.api.RAdapterException;
import com.simple.radapter.api.RConstants;

class RexpUtils {
	
	public static IRexp<?> convert(INativeEngine engine, long expression) throws RAdapterException{
		
		int expressionType = engine.getExpressionType(expression);
		
		if (expressionType == RConstants.STRSXP) {
			String[] strings = engine.getStrings(expression);
			if (strings.length == 1) {
				return new RexpString(strings[0]); 
			}
		
			RexpCollection<IRexpString> strCollection = new RexpCollection<IRexpString>();
			
			for (int i = 0; i < strings.length; i++) {
				strCollection.put(new RexpString(strings[i]));
			}
			
			return strCollection;
		}
		
		
		// R type 14 
		if (expressionType == RConstants.REALSXP) {
			double[] doubles = engine.getDoubles(expression);
			IRexpCollection<IRexp<Double>> doubleCollection = new RexpCollection<IRexp<Double>>();
			
			if (doubles.length == 1) {
				return new RexpDouble(doubles[0]);
			}
			
			for (double d : doubles) {
				doubleCollection.getValue().add(new RexpDouble(d));
			}
			
			return doubleCollection;
		}
		
		
		// Is an integer type
		if (expressionType == RConstants.INTSXP) {
			
		}
		
		if (expressionType == RConstants.REALSXP) {
			
		}

		
		throw new RAdapterException("Unable to find suitable conversion for expression, Type -> " + expressionType);
	}
}
