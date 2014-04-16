package com.simple.domain.model.ui.dashboard;

import java.util.Map;

import com.simple.api.exceptions.AnalyticsTaskException;
import com.simple.api.exceptions.DashboardException;
import com.simple.api.orchestrator.IMetric;

public class DashboardUtils {

    
    /**
     * This functionality will bind a dashboard to it's coordinating output
     * values. This essentially builds the dashboard output on the server side
     * and sends it back to the client for rendering. This will iterate over all
     * the widgets in the dashboard and ask the output map for it's
     * {@link Metric} value. This will be assigned to the widget metric to be
     * used during rendering.
     * 
     * @param dashboard
     *            The dashboard to bind the outputs to.
     * @param outputMap
     *            A key/value map of the outputs to bind the dashboard to.
     * @throws AnalyticsTaskException
     *             If the widget count is less than 1.
     */
    public static Dashboard bindDashboard(Dashboard dashboard, Map<Long, IMetric> outputMap) throws DashboardException {
        // Iterate over the dashboard widgets and build the dashboard to send
        // back to the client
    	/*

        if (dashboard == null) {
            return null;
        }

        List<Widget> allWidgets = dashboard.getAllWidgets();

        int widgetCount = allWidgets.size();

        if (widgetCount < 1) {
            throw new DashboardException("The dashboard that was specified does not contain any widgets so there is nothing to show."
                    + "Please edit the task's dashboard and configure some widgets to display");
        }

        for (Widget widget : allWidgets) {

            // TODO Important find a better way to do this, it is dumb to have
            // to cast the widget.
            if (widget instanceof MetricWidget) {
                MetricWidget<?> metricWidget = (MetricWidget<?>) widget;
                
                // Test to see if the widget has a metric assigned.
                if (metricWidget.getOutput() == null) {
                    metricWidget.setTitle("Not Assigned");
                    metricWidget.setDescription("There was no output assigned to this widget, please edit the dashbaord and configure an assignment");
                    continue;
                }

                IMetric metric = outputMap.get(metricWidget.getOutput().getId());

                if (metric == null) {
                	metricWidget.setTitle("Not Available");
                	metricWidget.setDescription("The metric was not found in the workspace for this widget ");
                	continue;
                }

                if (widget instanceof GaugeWidget) {
                    ((GaugeWidget) widget).setMetric((MetricDouble) metric);
                    continue;
                }

                if (widget instanceof MetricPlotWidget) {
                    ((MetricPlotWidget) widget).setMetric((MetricPlot) metric);
                    continue;
                }

                if (widget instanceof MetricTableWidget) {
                    ((MetricTableWidget) widget).setMetric((MetricMatrix) metric);
                    continue;
                }
            }
        }
        */
        return dashboard;
    }
}