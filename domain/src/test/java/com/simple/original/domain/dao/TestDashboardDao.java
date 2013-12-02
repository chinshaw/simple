package com.simple.original.domain.dao;

import com.google.inject.Inject;
import com.simple.domain.dashboard.DashboardDao;
import com.simple.original.api.exceptions.DomainException;

public class TestDashboardDao extends DaoTest {

	@Inject
	DashboardDao dashboardDao;
	
	public TestDashboardDao() throws DomainException {
		super();
	}

}
