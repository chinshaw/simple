package com.simple.domain.export;

import java.util.Collection;

import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.AnalyticsTask;
import com.simple.domain.model.Person;
import com.simple.domain.model.ui.dashboard.Dashboard;

public class ExportModel {

    /**
     * The version will typically be the svn revision.
     */
    private Long version;
    
    
    //private Collection<AnalyticsTask> tasks;
    
    //private Collection<AnalyticsOperation> operations;
    
    private Collection<Dashboard> dashbaords;
    
    private Collection<Person> persons;
    
    public ExportModel() {
    }
    
    public ExportModel(Long version) {
        this.version = version;
    }
    
    public Collection<AnalyticsTask> getTasks() {
        return null;
    }


    public void setTasks(Collection<AnalyticsTask> tasks) {
        //this.tasks = tasks;
    }


    public Collection<AnalyticsOperation> getOperations() {
        //return operations;
        return null;
    }


    public void setOperations(Collection<AnalyticsOperation> operations) {
        //this.operations = operations;
    }


    public Collection<Dashboard> getDashbaords() {
        return dashbaords;
    }

    public void setDashbaords(Collection<Dashboard> dashbaords) {
        this.dashbaords = dashbaords;
    }

    public void setPersons(Collection<Person> persons) {
        this.persons = persons;
    }
    
    public Collection<Person> getPersons() {
        return persons;
    }
    
    public Long getVersion() {
        return version;
    }
    
    public void setVersion(Long version) {
        this.version = version;
    }

}