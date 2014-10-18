package com.simple.orchestrator.api.hadoop.operation;

import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso(value = { Operation.class, ROperation.class, NullHadoopOperation.class })
public interface IOperation {

}
