package com.simple.domain.dao;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.simple.api.exceptions.DomainException;
import com.simple.domain.model.SqlConnection;
import com.simple.domain.model.SqlDriver;

public class SqlDataProviderDriverDao extends DaoBase<SqlConnection> {

	public SqlDataProviderDriverDao() {
		super(SqlConnection.class);
	}

	public List<String> getAvailableSqlDrivers() throws JAXBException {
		List<SqlDriver> drivers = SqlDriverDao.getSqlDrivers();
		List<String> driverNames = new ArrayList<String>();

		for (SqlDriver driver : drivers) {
			driverNames.add(driver.getDriverName());
		}
		return driverNames;
	}

	@Override
	public Long save(SqlConnection dataProvider) throws DomainException {
		return super.save(dataProvider);
	}

	public List<SqlConnection> find(int start, int max) {
		return super.findRange(start, max);
	}
}