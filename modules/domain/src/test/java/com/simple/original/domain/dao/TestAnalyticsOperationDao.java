package com.simple.original.domain.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.google.inject.Inject;
import com.simple.api.exceptions.DomainException;
import com.simple.domain.dao.AnalyticsOperationDao;
import com.simple.domain.model.AnalyticsOperationOutput;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.domain.model.ui.DateInput;
import com.simple.domain.model.ui.StringInput;
import com.simple.orchestrator.api.IRHadoopOperationOutput.Type;

public class TestAnalyticsOperationDao extends DaoTest {

	@Inject
	AnalyticsOperationDao operationDao;
	
	public TestAnalyticsOperationDao() throws DomainException {
		super();
	}
	
	@Test
	public void testSaveAndFetch() throws DomainException {
		RAnalyticsOperation op = new RAnalyticsOperation("testSaveAndFetch");
		op.setCode("Code" );
		op.setDescription("Description");
		
		op.addInput(new StringInput("Input1", "Value 1"));
		op.addInput(new StringInput("Input2", "Value 2"));
		op.addInput(new DateInput("Input3", new Date()));
		
		op.addOutput(new AnalyticsOperationOutput("Output1", AnalyticsOperationOutput.Type.NUMERIC));	
		op.addOutput(new AnalyticsOperationOutput("Output2", AnalyticsOperationOutput.Type.TEXT));
		op.addOutput(new AnalyticsOperationOutput("Output3", AnalyticsOperationOutput.Type.GRAPHIC));
		
		Long id = operationDao.save(op);
		
		assertNotNull(id);
		
		RAnalyticsOperation fetchedOp = (RAnalyticsOperation) operationDao.find(id);
		
		assertNotNull(fetchedOp);
		
		
		List<AnalyticsOperationInput> inputs = fetchedOp.getInputs();
		
		assertTrue(inputs.get(0).getInputName().equals("Input1"));
		assertTrue(inputs.get(1).getInputName().equals("Input2"));
		assertTrue(inputs.get(2).getInputName().equals("Input3"));
		
		List<AnalyticsOperationOutput> outputs = fetchedOp.getOutputs();
		
		assertTrue(outputs.get(0).getName().equals("Output1"));
		assertTrue(outputs.get(0).getOutputType().equals(Type.NUMERIC));
		assertTrue(outputs.get(1).getName().equals("Output2"));
		assertTrue(outputs.get(1).getOutputType().equals(Type.TEXT));
		assertTrue(outputs.get(2).getName().equals("Output3"));
		assertTrue(outputs.get(2).getOutputType().equals(Type.GRAPHIC));
	}
	
	@Test
	public void testDelete() throws DomainException {
		RAnalyticsOperation op = new RAnalyticsOperation("testSaveAndFetch");
		op.setCode("Code" );
		op.setDescription("Description");
		
		op.addInput(new StringInput("Input1", "Value 1"));
		op.addInput(new StringInput("Input2", "Value 2"));
		op.addInput(new DateInput("Input3", new Date()));
		
		op.addOutput(new AnalyticsOperationOutput("Output1", AnalyticsOperationOutput.Type.NUMERIC));	
		op.addOutput(new AnalyticsOperationOutput("Output2", AnalyticsOperationOutput.Type.TEXT));
		op.addOutput(new AnalyticsOperationOutput("Output3", AnalyticsOperationOutput.Type.GRAPHIC));
		
		Long id = operationDao.save(op);
		
		assertNotNull(id);
		
		RAnalyticsOperation fetchedOp = (RAnalyticsOperation) operationDao.find(id);
		
		assertNotNull(fetchedOp);
		
		
		List<AnalyticsOperationInput> inputs = fetchedOp.getInputs();
		
		assertTrue(inputs.get(0).getInputName().equals("Input1"));
		assertTrue(inputs.get(1).getInputName().equals("Input2"));
		assertTrue(inputs.get(2).getInputName().equals("Input3"));
		
		List<AnalyticsOperationOutput> outputs = fetchedOp.getOutputs();
		
		assertTrue(outputs.get(0).getName().equals("Output1"));
		assertTrue(outputs.get(0).getOutputType().equals(Type.NUMERIC));
		assertTrue(outputs.get(1).getName().equals("Output2"));
		assertTrue(outputs.get(1).getOutputType().equals(Type.TEXT));
		assertTrue(outputs.get(2).getName().equals("Output3"));
		assertTrue(outputs.get(2).getOutputType().equals(Type.GRAPHIC));
	}
}
