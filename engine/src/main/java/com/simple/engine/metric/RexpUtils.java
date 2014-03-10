package com.simple.engine.metric;

import com.simple.engine.api.IMetric;
import com.simple.radapter.protobuf.REXP;

public class RexpUtils {

	public static IMetric toMetric(REXP rexp) {

		switch (rexp.getRclass()) {
		case RAWSXP:
			return new MetricRaw(rexp.getRawValue().toByteArray());
		case ANYSXP:
			break;
		case BCODESXP:
			break;
		case BUILTINSXP:
			break;
		case CHARSXP:
			break;
		case CLOSXP:
			break;
		case CPLXSXP:
			break;
		case DOTSXP:
			break;
		case ENVSXP:
			break;
		case EXPRSXP:
			break;
		case EXTPTRSXP:
			break;
		case FREESXP:
			break;
		case FUNSXP:
			break;
		case INTSXP:
			break;
		case LANGSXP:
			break;
		case LGLSXP:
			break;
		case LISTSXP:
			break;
		case NEWSXP:
			break;
		case NILSXP:
			break;
		case PROMSXP:
			break;
		case REALSXP:
			break;
		case S4SXP:
			break;
		case SPECIALSXP:
			break;
		case STRSXP:
			break;
		case SYMSXP:
			break;
		case VECSXP:
			break;
		case WEAKREFSXP:
			break;
		default:
			break;
		}
		return null;
	}
}
