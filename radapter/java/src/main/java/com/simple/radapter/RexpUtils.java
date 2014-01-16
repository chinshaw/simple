package com.simple.radapter;

import com.simple.radapter.api.IRAdapter;
import com.simple.radapter.api.IRexp;
import com.simple.radapter.api.RConstants;
import com.simple.radapter.exceptions.RAdapterException;

public class RexpUtils {

	
	
	public static IRexp<?> convert(IRAdapter adapter, long expression) throws RAdapterException{
		
		int expressionType = adapter.getExpressionType(expression);
		
		if (expressionType == RConstants.STRSXP) {
			
		}
		
		
		throw new RAdapterException("Unable to find suitable conversion for expression");
	}
}
