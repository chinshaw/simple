package com.simple.original.server.service;


import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;

import com.google.inject.Inject;
import com.simple.domain.AnalyticsOperationOutput;
import com.simple.domain.AnalyticsTask;
import com.simple.domain.Person;
import com.simple.domain.RAnalyticsOperation;
import com.simple.domain.dashboard.Dashboard;
import com.simple.engine.utils.ScriptUtils;
import com.simple.original.api.analytics.IAnalyticsOperationOutput.Type;
import com.simple.original.api.exceptions.DashboardException;
import com.simple.original.api.exceptions.DomainException;
import com.simple.web.test.TestInjectorFactory;

public class TestDashboardService {

	@Inject
	DashboardService service;
	
	@Inject
	Logger logger;
	
	public TestDashboardService() {
		TestInjectorFactory.getInjector().injectMembers(TestDashboardService.this);
	}
	
	@Test
	public void testExecuteInteractive() throws DomainException, DashboardException, IOException {
		Dashboard dashboard = createDashboard();
		service.executeInteractive(dashboard, null, null);

		
	}

	
	/**
	 * Create a blank dashboaard with some basic widgets.
	 * @return
	 * @throws DomainException 
	 * @throws IOException 
	 */
	private Dashboard createDashboard() throws DomainException, IOException {
		Person person = new Person("TEST OWNER", "test@foo.com");
		
		AnalyticsTask task = new AnalyticsTask("TEST TASK");
		task.setOwner(person);
		
		RAnalyticsOperation operation = new RAnalyticsOperation("TEST OPERATION");
		operation.setOwner(person);
		operation.setCode(ScriptUtils.getScriptCode("/rscripts/BollingerScript.R"));
		operation.addOutput(new AnalyticsOperationOutput("testMetric",Type.NUMERIC));
		task.addOperation(operation);
		//task = taskDao.saveAndReturn(task);
		
		
		Dashboard dashboard = new Dashboard("TEST DASHBOARD");
		dashboard.setAnalyticsTask(task);
		
		return dashboard;
	}
}
