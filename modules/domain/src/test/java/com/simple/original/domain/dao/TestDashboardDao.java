package com.simple.original.domain.dao;

import com.google.inject.Inject;
import com.simple.api.exceptions.DomainException;
import com.simple.domain.model.ui.dashboard.DashboardDao;

public class TestDashboardDao extends DaoTest {

	@Inject
	DashboardDao dashboardDao;
	
	public TestDashboardDao() throws DomainException {
		super();
	}

}
