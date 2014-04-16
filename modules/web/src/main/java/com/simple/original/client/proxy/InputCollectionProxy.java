package com.simple.original.client.proxy;

import java.util.List;

public interface InputCollectionProxy {

    public void setInputs(List<AnalyticsOperationInputProxy> inputs);
    
    public List<AnalyticsOperationInputProxy> getInputs();
}
