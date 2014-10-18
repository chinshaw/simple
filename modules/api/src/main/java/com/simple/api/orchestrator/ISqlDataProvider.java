package com.simple.api.orchestrator;

import com.simple.orchestrator.api.dataprovider.IDataProvider;


public interface ISqlDataProvider extends IDataProvider {

    public abstract String getSqlStatement();

    public abstract void setSqlStatement(String sqlStatement);

}