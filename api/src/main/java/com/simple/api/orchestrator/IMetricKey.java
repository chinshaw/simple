package com.simple.api.orchestrator;

import java.io.Serializable;


public interface IMetricKey extends Serializable  {

	byte[] toBytes();
}
