package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.model.SqlConnection;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;

@ProxyFor(value=SqlConnection.class, locator=RequestFactoryEntityLocator.class)
public interface SqlConnectionProxy extends DatastoreObjectProxy {

    /**
     * This is the name of the data provider for example.
     * "PRCTLS READ ONLY"
     * @return
     */
    public String getName();
    
    /**
     * Setter for the name of the data provider.
     * @param name
     */
    public void setName(String name);
    
    /**
     * This is the user name for the connection.
     * @return
     */
    public String getUserName();
    
    /**
     * Setter for the user name.
     * @param user
     */
    public void setUserName(String user);

    /**
     * Getter for the password.
     * @return
     */
    public String getPassword();

    /**
     * Setter for the password.
     * @param password
     */
    public void setPassword(String password);

    /**
     * Connection host this can be ip or hostname.
     * @return
     */
    public String getHost();

    /**
     * Setter for the host name.
     * @param host
     */
    public void setHost(String host);

    /**
     * The driver to use for the connection.
     * @return
     */
    public String getDriverName();

    /**
     * Setter for the driver.
     * @param driver
     */
    public void setDriverName(String driverName);
}
