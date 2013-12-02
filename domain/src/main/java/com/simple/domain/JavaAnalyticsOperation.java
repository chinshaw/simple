package com.simple.domain;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class JavaAnalyticsOperation extends AnalyticsOperation {

    /**
     * Serialization Id.
     */
    private static final long serialVersionUID = 4640283928839390660L;
    /**
     * This is the text code that is to be run
     */
    protected String executableJar;

    /**
     * Default constructor
     */
    public JavaAnalyticsOperation() {        
    }

    public String getExecutableJar() {
        return executableJar;
    }
    
    public void setExecutableJar(String executableJar) {
        this.executableJar = executableJar;
    }

	@Override
	public void setOwner(Person currentPerson) {
		// TODO Auto-generated method stub
		
	}
    
}