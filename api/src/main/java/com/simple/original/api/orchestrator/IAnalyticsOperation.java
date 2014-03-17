package com.simple.original.api.orchestrator;

import java.util.List;



/**
 * Base interface interace from all analytics operation
 * types. Right now this is simply RAnalyticsOperation but this
 * will expand to supporting multiple types of operations.
 * @author chinshaw
 * 
 */
public interface IAnalyticsOperation extends IRequestFactoryEntity {

    /**
     * The name of the operation.
     * @return
     */
    public String getName();

    /**
     * Description of the analytics operation.
     * @return
     */
    public String getDescription();

    /**
     * Get analytics task inputs.
     * 
     * @return
     */
    public List<? extends IAnalyticsOperationInput> getInputs();

    /**
     * Getter for a list of outputs.
     * 
     * @return List of outputs to render.
     */
    public List<? extends IAnalyticsOperationOutput> getOutputs();
    
    /**
     * Getter for data provider inputs.
     * 
     * @return
     */
    public List<? extends IDataProviderInput> getDataProviders();
}