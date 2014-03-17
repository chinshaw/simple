package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.model.AnalyticsOperationOutput;
import com.simple.original.api.orchestrator.IAnalyticsOperationOutput;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;

/**
 * Interface for the {@link AnalyticsOperationOutput} class. This is the
 * Requestfactory client side interface to using the
 * {@link AnalyticsOperaitonOuptut} object. This object is used on the client
 * side for designign the dashboard and on the server side to figure out what
 * outputs should be collected and their types.
 * 
 * @author chinshaw
 */
@ProxyFor(value = AnalyticsOperationOutput.class, locator = RequestFactoryEntityLocator.class)
public interface AnalyticsOperationOutputProxy extends DatastoreObjectProxy {

    /**
     * Get the user-defined type of the output, this is a recomnended output
     * type but is not required when defining outputs for the operation.
     * 
     * @return The output
     */
    public IAnalyticsOperationOutput.Type getOutputType();

    /**
     * Set the output type for the operation this is used as a recommended
     * output type for the output. This is especially important when trying to
     * design the dashboard because certain widgets only support particular
     * types of outputs.
     * 
     * @param outputType
     */
    public void setOutputType(IAnalyticsOperationOutput.Type outputType);

    /**
     * The name of the output. This is used to find the output when the
     * operation is complete.
     * 
     * @return The name of the output
     */
    public String getName();

    /**
     * The name / variable name of the output.
     * 
     * @param name
     *            The name of the output
     */
    public void setName(String name);

    /**
     * Is the metric required when evaluating the outputs.
     * 
     * @return true if metric is required to be present when operation is
     *         complete, false if it is not.
     */
    public boolean isRequired();

    /**
     * Set whether the metric is required when evaluating the outputs from the
     * operation.
     * 
     * @param required
     *            whether the metric is required
     */
    public void setRequired(boolean required);
}