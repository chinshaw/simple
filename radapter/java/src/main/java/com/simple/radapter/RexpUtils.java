package com.simple.radapter;

import com.simple.radapter.api.IEngine;
import com.simple.radapter.api.IRexp;
import com.simple.radapter.api.IRexpString;
import com.simple.radapter.api.RAdapterException;
import com.simple.radapter.api.RConstants;

class RexpUtils {
	
	public static IRexp<?> convert(IEngine adapter, long expression) throws RAdapterException{
		
		int expressionType = adapter.getExpressionType(expression);
		
		if (expressionType == RConstants.STRSXP) {
			String[] strings = adapter.getStrings(expression);
			if (strings.length == 1) {
				return new RexpString(strings[0]); 
			}
			
		
			RexpCollection<IRexpString> strCollection = new RexpCollection<IRexpString>();
			
			for (int i = 0; i < strings.length; i++) {
				strCollection.put(new RexpString(strings[i]));
			}
			
			return strCollection;
		}
		
		// Is an integer type
		if (expressionType == RConstants.INTSXP) {
			
		}
		
		if (expressionType == RConstants.REALSXP) {
			
		}

		
		throw new RAdapterException("Unable to find suitable conversion for expression, Type -> " + expressionType);
	}
}
