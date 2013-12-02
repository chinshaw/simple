package com.simple.original.client.proxy;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.AnalyticsOperation;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;

@ProxyFor(value = AnalyticsOperation.class, locator = RequestFactoryEntityLocator.class)
public interface AnalyticsOperationProxy extends DatastoreObjectProxy {

    public static String[] EXEC_PROPERTIES = { "inputs" };

    public static String[] EDIT_PROPERTIES = { "inputs", "inputs.inputs", "outputs", "dataProviders" };

    public enum OperationType {
        R_Analytics("R Script"), Java("Java Executable Jar");

        private final String description;

        OperationType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
        
    public abstract String getName();

    public abstract void setName(String name);

    public abstract String getDescription();

    public abstract void setDescription(String description);

    /**
     * Get script inputs.
     * 
     * @return
     */
    public abstract List<AnalyticsOperationInputProxy> getInputs();

    /**
     * Set script inputs.
     * 
     * @param inputs
     */
    public void setInputs(List<AnalyticsOperationInputProxy> inputs);

    /**
     * Getter for the script outputs.
     * 
     * @return
     */
    public List<AnalyticsOperationOutputProxy> getOutputs();

    /**
     * Setter for the outputs.
     * 
     * @param scriptOutputs
     */
    public void setOutputs(List<AnalyticsOperationOutputProxy> outputs);

    /**
     * Getter for isPublic flag.
     * 
     * @return
     */
    public boolean isPublic();

    /**
     * Setter for isPublic flag.
     * 
     * @param isPublic
     */
    public void setPublic(boolean isPublic);

    /**
     * Getter for the Person.
     * 
     * @return
     */
    public PersonProxy getOwner();

    
}
