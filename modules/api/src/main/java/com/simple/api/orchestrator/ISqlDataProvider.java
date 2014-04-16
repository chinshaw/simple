package com.simple.api.orchestrator;


public interface ISqlDataProvider extends IDataProvider {

    public abstract String getSqlStatement();

    public abstract void setSqlStatement(String sqlStatement);

}