package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.model.ui.dashboard.NumberRange;
import com.simple.original.api.orchestrator.Criticality;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;


@ProxyFor(value = NumberRange.class, locator = RequestFactoryEntityLocator.class)
public interface NumberRangeProxy extends DatastoreObjectProxy {
    /**
     * <p>
     * Returns the minimum number in this range.
     * </p>
     * 
     * @return the minimum number in this range
     */
    public Double getMinimum();

    
    public void setMinimum(Double value);
    
    /**
     * <p>
     * Returns the maximum number in this range.
     * </p>
     * 
     * @return the maximum number in this range
     */
    public Double getMaximum();
    
    
    public void setMaximum(Double value);
    
    public Criticality getCriticality();
    
    public void setCriticality(Criticality criticality);
}
