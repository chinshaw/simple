package com.simple.original.api.analytics;


public interface ISqlDataProvider extends IDataProvider {

    public abstract String getSqlStatement();

    public abstract void setSqlStatement(String sqlStatement);

}