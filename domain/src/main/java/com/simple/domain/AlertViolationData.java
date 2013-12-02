package com.simple.domain;

public class AlertViolationData extends DatastoreObject {

    /**
     * Serialization Id
     */
    private static final long serialVersionUID = -8678324823680909700L;

    public static final String RULE_NAME = "RULE";
    public static final String CHART_STATISTICS = "CHART_STATISTICS";
    public static final String START_DATE = "START_DATE";

    private String ruleName;
    private String chartStatistics;
    private String startDate;

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getChartStatistics() {
        return chartStatistics;
    }

    public void setChartStatistics(String chartStatistics) {
        this.chartStatistics = chartStatistics;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getPCenter() {
        return "NA";
    }

    public String getUCL() {
        return "NA";
    }

    public String getLCL() {
        return "NA";
    }
}
