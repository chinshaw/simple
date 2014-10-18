package com.simple.orchestrator.api.hadoop.operation;

import javax.xml.bind.annotation.XmlSeeAlso;


@XmlSeeAlso({ROperation.class, NullHadoopOperation.class})
public abstract class Operation implements IOperation {

	
	public abstract String getJobName();
}
