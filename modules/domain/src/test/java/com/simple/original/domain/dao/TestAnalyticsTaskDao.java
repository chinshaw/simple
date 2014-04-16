package com.simple.original.domain.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Inject;
import com.simple.api.exceptions.DomainException;
import com.simple.api.exceptions.SimpleException;
import com.simple.api.orchestrator.IAnalyticsOperationOutput;
import com.simple.domain.dao.AnalyticsOperationDao;
import com.simple.domain.dao.AnalyticsTaskDao;
import com.simple.domain.model.AnalyticsOperationOutput;
import com.simple.domain.model.AnalyticsTask;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.domain.model.ui.ComplexInput;
import com.simple.domain.model.ui.DateInput;
import com.simple.domain.model.ui.StringInput;
import com.simple.domain.model.ui.dashboard.Dashboard;
import com.simple.security.api.ISession;

public class TestAnalyticsTaskDao extends DaoTest {


	@Inject
	AnalyticsOperationDao operationDao;

	@Inject
	AnalyticsTaskDao taskDao;

	@Inject
	ISession session;


	@Inject
	EntityManagerFactory entityManagerFactory;

	public TestAnalyticsTaskDao() throws DomainException {

	}

	/**
	 * Create an entity manager factory using the TestPersistenceManager
	 * factory, this database can be blown away as necessary.
	 * 
	 * @throws DomainException
	 */
	@BeforeClass
	public static void init() throws DomainException {

	}

	@AfterClass
	public static void tearDown() {

	}

	@After
	public void cleanup() {
		// truncateAllTables(entityManagerFactory.createEntityManager());
	}

	@Test
	public void testSaveAndFetch() throws DomainException {

		RAnalyticsOperation operation = new RAnalyticsOperation("Name");
		operationDao.save(operation);

		AnalyticsTask task = new AnalyticsTask("testSaveAndFetch");
		task.setOwner(testPerson);
		task.getOperations().add(operation);

		Long id = taskDao.save(task);

		Assert.assertNotNull(id);
		task = taskDao.find(id);

		System.out.println("ChangeLogs are " + task.getChangeLogs());
		System.out.println("ChangeLogs are " + task.getChangeLogs().size());

		Assert.assertTrue("Did not contain a change log, should have had one",
				task.getChangeLogs().size() >= 1);
	}

	@Test
	public void testFetchTaskWithInputs() throws DomainException {
		RAnalyticsOperation operation = new RAnalyticsOperation("Name");
		operation.addInput(new DateInput("date input 1", new Date()));
		operation.addInput(new StringInput("strning input 1", "The value"));
		operation.addInput(new ComplexInput("complex input 1"));
		operationDao.save(operation);

		AnalyticsTask task = new AnalyticsTask("testFetchTaskWithInputs");
		task.setOwner(testPerson);
		task.getOperations().add(operation);

		Long id = taskDao.save(task);

		Assert.assertNotNull(id);
		task = taskDao.find(id);

		assertNotNull(task.getOperations());

		RAnalyticsOperation fetchedOperation = (RAnalyticsOperation) task
				.getOperations().get(0);
		assertNotNull(fetchedOperation);
		List<AnalyticsOperationInput> inputs = fetchedOperation.getInputs();
		assertNotNull(inputs);
		assertTrue(inputs.size() == 3);

	}

	@Test
	public void testCloneTask() throws CloneNotSupportedException,
			SimpleException, DomainException {

		RAnalyticsOperation operation = new RAnalyticsOperation("Operation Name");
		
		operation.getOutputs().add(
				new AnalyticsOperationOutput("output1",
						IAnalyticsOperationOutput.Type.NUMERIC));
		
		Long id = operationDao.save(operation);

		operation = (RAnalyticsOperation) operationDao.find(id);
		Assert.assertTrue(operation.getId() != null);
		
		// Check that the output has a valid id.
		AnalyticsOperationOutput opOutput = operation.getOutputs().get(0);
		Assert.assertTrue(opOutput.getId() != null);

		AnalyticsTask task = new AnalyticsTask("testCloneTask");
		task.addOperation(operation);

		Dashboard dashboard = new Dashboard("The dashboard");

		task.setDashboard(dashboard);
		dashboard.setAnalyticsTask(task);

		Long taskId = taskDao.save(task);

		Assert.assertNotNull(taskId);

		// Go fetch the task again.
		task = taskDao.find(taskId);

		
		AnalyticsTask cloned = taskDao.copy(task.getId());
		assertNotNull(cloned);

		// Id of new task must be null.
		assertTrue(cloned.getId().longValue() != taskId.longValue());

		// Operation should have it's id still set
		assertTrue(cloned.getOperations().get(0).getId() != null);
	}
}
