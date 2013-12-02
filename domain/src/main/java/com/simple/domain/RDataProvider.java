package com.simple.domain;

import javax.persistence.Entity;


@Entity
public class RDataProvider extends DataProvider {
    
    /**
     * Serialization id.
     */
    private static final long serialVersionUID = -9192016874187659793L;
    
    /**
     * The command that will be executed to assign data into the workspace.
     */
    private String command;

    public RDataProvider() {
    }

    public String getRCommand() {
        return command;
    }

    public void setRCommand(String command) {
        this.command = command;
    }

}
