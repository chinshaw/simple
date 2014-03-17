package com.simple.original.api.orchestrator;


public interface IViolation {
    
    
    public enum Severity {
        WARING,
        MAJOR,
        CRITICAL,
        GENERAL
    }

    public static final String RTYPE = "Violation";
    public static final String RTYPE_RULE = "rule";
    public static final String RTYPE_DETAIL = "detail";
    public static final String RTYPE_SUBGROUP = "subgroup";
    public static final String RTYPE_SEVERITY = "severity";

    public abstract Severity getSeverity();

    public abstract void setSeverity(Severity severity);

    public abstract String getDetail();

    public abstract void setDetail(String detail);

    public abstract String getRuleName();

    public abstract void setRuleName(String rule);

    public abstract Integer getSubgroup();

    public abstract void setSubgroup(Integer subgroup);

}