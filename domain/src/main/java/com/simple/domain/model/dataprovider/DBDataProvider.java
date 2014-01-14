package com.simple.domain.model.dataprovider;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class DBDataProvider extends DataProvider {

	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = -6747792655749738919L;

	@OneToOne
	private DBConnection connection;

	private String tableName;

	private String inputQuery;

	private String outputTableName;

	public DBDataProvider() {

	}

	public DBDataProvider(DBConnection connection) {
		this.connection = connection;
	}

	public DBConnection getConnection() {
		return connection;
	}

	public void setConnection(DBConnection connection) {
		this.connection = connection;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getInputQuery() {
		return inputQuery;
	}

	public void setInputQuery(String inputQuery) {
		this.inputQuery = inputQuery;
	}

	public String getOutputTableName() {
		return outputTableName;
	}

	public void setOutputTableName(String outputTableName) {
		this.outputTableName = outputTableName;
	}
}
