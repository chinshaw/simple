package com.simple.domain.dashboard;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.simple.domain.RequestFactoryEntity;

@Entity
@Table(name = "dashboard_linkabletask")
public class LinkableDashboard extends RequestFactoryEntity {

    /**
     * Serialization ID
     */
    private static final long serialVersionUID = -5578003389364834027L;

    /**
     * The dashboard to link to.
     */
    @OneToOne
    private Dashboard dashboard;

    private String context;
    
    public LinkableDashboard() {
    }
    
    public Dashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
    
    public Long getDashboardId() {
        return (dashboard == null ? null : dashboard.getId());
    }
}