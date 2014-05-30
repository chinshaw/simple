package com.simple.orchestrator.utils;

import com.simple.orchestrator.api.metric.Metric;
import com.simple.orchestrator.api.metric.MetricKey;
import com.simple.orchestrator.api.metric.MetricNumber;
import com.simple.orchestrator.api.metric.MetricRaw;
import com.simple.orchestrator.api.metric.MetricString;
import com.simple.radapter.protobuf.REXPProtos.Rexp;

public class RexpUtils {

	public static Metric toMetric(MetricKey key, Rexp rexp) {

		switch (rexp.getRclass()) {
		case RAWSXP:
			return new MetricRaw(key, rexp.getRawValue().toByteArray());
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
			return new MetricNumber(rexp.getRealValueList());
		case S4SXP:
			break;
		case SPECIALSXP:
			break;
		case STRSXP:
			return new MetricString(rexp.getStringValueList());
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
