package com.simple.original.server;

import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;
import com.simple.domain.model.metric.Metric;
import com.simple.domain.model.metric.MetricString;

public class AnalyticsServiceDecorator extends ServiceLayerDecorator {

    /**
     * This is a semi hack work around to resolve client types for the
     * Revolution library. This is needed to resolve domain objects to client
     * types. Since I cannot simply cast an object to it's interface and change
     * it's complete signature this is need to resolve where the client proxy
     * lives. What this does is look into an object to see if it has any
     * interfaces then it loops over them looking for one assignable that is a
     * superclass of RData. If this is true it will use the interface to resolve
     * the proxy correctly.
     * 
     * As far as I know as long as we will have two library implementations this
     * will be necessary. It does add some slowness to the resolving of clients.
     * 
     * 
     * TODO revisit this solution and determine if there is a better/faster way
     * of resolving client proxies.
     */
    @Override
    public <T> Class<? extends T> resolveClientType(Class<?> domainClass, Class<T> clientType, boolean required) {
        Class<?> interfaces[] = domainClass.getInterfaces();
        for (Class<?> interf : interfaces) {
            System.out.println("Found interface of type" + interf.getName());
            if (!MetricString.class.isAssignableFrom(domainClass)) {
                System.out.println("Domain class is not an RDataString");
                if (Metric.class.isAssignableFrom(interf)) {
                    System.out.println(interf.getName() + " is assignable to Rdata");
                    return super.resolveClientType(interf, clientType, required);
                }
            }
        }
        return super.resolveClientType(domainClass, clientType, required);
    }
}
