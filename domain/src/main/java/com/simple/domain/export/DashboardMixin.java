package com.simple.domain.export;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.simple.domain.AnalyticsTask;
import com.simple.domain.dashboard.Dashboard;
import com.simple.domain.dashboard.Widget;


/**
 * Mixin class to provide annotations for {@link Dashboard}
 * @author chinshaw
 *
 */
public interface DashboardMixin {


    @JsonGetter("analyticsTask")
    public AnalyticsTask getAnalyticsTask();
    
    @JsonSetter("analyticsTask")
    public void setAnalyticsTask(AnalyticsTask task);
    
    @JsonIgnore
    public List<Widget> getAllWidgets();
    
}
