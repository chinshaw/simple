package com.simple.domain;

import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
public class SqlDataProvider extends DataProvider {

    /**
	 * Serialization Id.
	 */
	private static final long serialVersionUID = -4341671376424639831L;
	
	/**
     * This is the sql statement that willb e executed by the 
     * provider.
     */
	@Column(columnDefinition = "TEXT")
    private String sqlStatement;

    public SqlDataProvider() {
    }

    public String getSqlStatement() {
        return sqlStatement;
    }

    public void setSqlStatement(String sqlStatement) {
        this.sqlStatement = sqlStatement;
    }
}