package com.simple.domain.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class SqlConnection extends RequestFactoryEntity {

    /**
     * Serialization Id
     */
    private static final long serialVersionUID = 8975480587594469532L;

    @NotNull(message = "Name is required")
    private String name;

    @Size(max = 256)
    private String description;

    @NotNull(message = "Driver must be selected")
    private String driverName;

    @NotNull(message = "Username is required")
    private String userName;

    @NotNull(message = "Password is required")
    private String password;

    @NotNull(message = "Host is required")
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

    /**
     * Getter for the description of the sql connection.
     * 
     * @return Description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the description of the sql connection.
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}