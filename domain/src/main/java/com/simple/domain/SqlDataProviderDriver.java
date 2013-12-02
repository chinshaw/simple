package com.simple.domain;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class SqlDataProviderDriver extends DatastoreObject {

    @NotNull(message= "Name is required")
    private String name;
    
    @NotNull(message = "Driver must be selected")
    private String driverName;
    
    @NotNull(message="Username is required")
    private String userName;
    
    @NotNull(message="Password is required")
    private String password;
    
    @NotNull(message="Host is required")
    private String host;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
}