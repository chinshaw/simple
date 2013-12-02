package com.simple.original.domain.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import com.google.inject.Inject;
import com.simple.domain.dao.MetricDao;
import com.simple.domain.metric.MetricDouble;
import com.simple.domain.metric.MetricInteger;
import com.simple.domain.metric.MetricMatrix;
import com.simple.domain.metric.MetricString;
import com.simple.domain.metric.Violation;
import com.simple.domain.metric.MetricMatrix.Column;
import com.simple.domain.metric.MetricMatrix.DoubleColumn;
import com.simple.domain.metric.MetricMatrix.IntegerColumn;
import com.simple.original.api.analytics.IViolation.Severity;
import com.simple.original.api.exceptions.DomainException;

public class TestMetricDao extends DaoTest {

	@Inject
	MetricDao dao;
	
    public TestMetricDao() throws DomainException {
		super();
		// TODO Auto-generated constructor stub
	}

	private static final String TEST_VIOLATION_RULE_NAME = "RULE 1";
    private static final String TEST_VIOLATION_DETAIL = TEST_VIOLATION_RULE_NAME + " was broken at ";
    private static final Integer TEST_VIOLATION_SUBGROUP = 22;
    private static final Severity TEST_VIOLATION_SEVERITY = Severity.CRITICAL;


    /**
     * Test that we can save a metric string to the datbase and that it comes
     * back the same way with violations and others.
     * @throws DomainException 
     */
    @Test
    public void testSaveMetricString() throws DomainException {
        final String METRIC_NAME = "metric1";
        final String METRIC_VALUE = "METRIC STRING VALUE";

        MetricString metric = new MetricString();

        metric.setName(METRIC_NAME);
        metric.setValue(METRIC_VALUE);

        // Simple test of the setters.
        assertTrue(METRIC_NAME.equals(metric.getName()));
        assertTrue(METRIC_VALUE.equals(metric.getValue()));

        metric.getViolations().add(createTestViolation());
        assertTrue(metric.getViolations().size() > 0);

        metric = (MetricString) dao.saveAndReturn(metric);

        // Fetch the saved object from the db and verify the attributes were
        // saved.s
        metric = (MetricString) dao.find(metric.getId());

        assertNotNull(metric.getId());

        assertTrue(metric.getName().equals(METRIC_NAME));
        assertTrue(metric.getValue() == METRIC_VALUE);

        // Check Violation values
        assertTrue(metric.getViolations().size() == 1);

        Violation violation = metric.getViolations().get(0);
        assertTrue(violation.getDetail().equals(TEST_VIOLATION_DETAIL));
        assertTrue(violation.getSeverity().equals(TEST_VIOLATION_SEVERITY));
        assertTrue(violation.getSubgroup() == TEST_VIOLATION_SUBGROUP);
    }

    /**
     * Test that we can save a metric number to the database and that it comes
     * back the same way.
     * @throws DomainException 
     * 
     */
    @Test
    public void testSaveMetricDouble() throws DomainException {
        final String METRIC_NAME = "metric1";
        final Double METRIC_VALUE = 123456.123454;

        MetricDouble metric = new MetricDouble();

        metric.setName(METRIC_NAME);
        metric.setValue(METRIC_VALUE);

        // Simple test of the setters.
        assertTrue(METRIC_NAME.equals(metric.getName()));
        assertTrue(METRIC_VALUE.equals(metric.getValue()));

        metric.getViolations().add(createTestViolation());
        assertTrue(metric.getViolations().size() > 0);

        // Save the object
        metric = (MetricDouble) dao.saveAndReturn(metric);
       
        // Fetch the saved object from the db and verify the attributes were
        // saved.s
        metric = (MetricDouble) dao.find(metric.getId());

        assertNotNull(metric.getId());

        assertTrue(metric.getName().equals(METRIC_NAME));
        assertTrue(metric.getValue() == METRIC_VALUE);

        // Check Violation values
        assertTrue(metric.getViolations().size() == 1);

        Violation violation = metric.getViolations().get(0);
        assertTrue(violation.getDetail().equals(TEST_VIOLATION_DETAIL));
        assertTrue(violation.getSeverity().equals(TEST_VIOLATION_SEVERITY));
        assertTrue(violation.getSubgroup() == TEST_VIOLATION_SUBGROUP);
    }
    
    @Test
    public void testSaveMetricMatrix() throws DomainException {
        
        MetricMatrix matrix = new MetricMatrix("testSaveMetricMatrix");
        
        int ints[] =  new int[100];
        for (int i = 0, j=100; i < 100; i++, j++) {
            ints[i] = j;
        }
        
        matrix.addColumn(new IntegerColumn("Int column", ints));
        
        matrix = (MetricMatrix) dao.saveAndReturn(matrix);
        
        MetricMatrix testingIntegerMatrix = (MetricMatrix) dao.find(matrix.getId());
        
        int numColumns = testingIntegerMatrix.getColumns().size();
        Assert.assertTrue("Matrix should have 1 column but has " + numColumns, numColumns == 1);
        Column<?> firstColumn = testingIntegerMatrix.getColumns().get(0);
        Assert.assertTrue("First column must be of type integer column", firstColumn instanceof IntegerColumn);
        IntegerColumn intColumn = (IntegerColumn) firstColumn;
        MetricInteger firstValue = intColumn.getCells().get(0);
        Assert.assertTrue("First value should have been " + 100 + " but was " + firstValue.getValue(), firstValue.getValue() == 100);
        
        System.out.println(intColumn.toString());
        
        // Add double column and check values.
        double doubles[] = new double[100];
        double dv = 100000.100;
        for (int i = 0; i < 100; i++) {
            doubles[i] = dv;
            dv += .001;
        }
        
        testingIntegerMatrix.addColumn(new DoubleColumn("Double column", doubles));
        
        testingIntegerMatrix = (MetricMatrix) dao.saveAndReturn(testingIntegerMatrix);
        
        MetricMatrix testingDoubleMatrix = (MetricMatrix) dao.find(testingIntegerMatrix.getId());
        
        
        numColumns = testingDoubleMatrix.getColumns().size();
        Assert.assertTrue("Matrix should have 2 column but has " + numColumns, numColumns == 2);
        Column<?> secondColumn = testingDoubleMatrix.getColumns().get(1);
        Assert.assertTrue("First column must be of type integer column", secondColumn instanceof DoubleColumn);
        DoubleColumn doubleColumn = (DoubleColumn) secondColumn;
        MetricDouble firstDoubleValue = doubleColumn.getCells().get(0);
        Assert.assertEquals("First value should have been 100000.100 " +  firstDoubleValue.getValue(), firstDoubleValue.getValue(),100000.100, .1);
    }
    

    /**
     * Test that we can save a metric number to the database and that it comes
     * back the same way.
     * @throws DomainException 
     * 
     */
    @Test
    public void testSaveMetricInteger() throws DomainException {
        final String METRIC_NAME = "metricInteger";
        final Integer METRIC_VALUE = 123456;

        MetricInteger metric = new MetricInteger();

        metric.setName(METRIC_NAME);
        metric.setValue(METRIC_VALUE);

        // Simple test of the setters.
        assertTrue(METRIC_NAME.equals(metric.getName()));
        assertTrue(METRIC_VALUE.equals(metric.getValue()));

        metric.getViolations().add(createTestViolation());
        assertTrue(metric.getViolations().size() > 0);


        metric = (MetricInteger) dao.saveAndReturn(metric);

        // Fetch the saved object from the db and verify the attributes were
        // saved.s
        metric = (MetricInteger) dao.find(metric.getId());

        assertNotNull(metric.getId());

        assertTrue(metric.getName().equals(METRIC_NAME));
        assertTrue(metric.getValue() == METRIC_VALUE);

        // Check Violation values
        assertTrue(metric.getViolations().size() == 1);

        Violation violation = metric.getViolations().get(0);
        assertTrue(violation.getDetail().equals(TEST_VIOLATION_DETAIL));
        assertTrue(violation.getSeverity().equals(TEST_VIOLATION_SEVERITY));
        assertTrue(violation.getSubgroup() == TEST_VIOLATION_SUBGROUP);
    }

    @Test
    public void doItMultipleTimes() throws DomainException {
        for (int i = 0; i < 100; i++) {
            testSaveMetricInteger();
            testSaveMetricDouble();
            testSaveMetricString();
        }
    }

    private static Violation createTestViolation() {
        Violation v = new Violation();

        v.setRuleName(TEST_VIOLATION_RULE_NAME);

        v.setDetail(TEST_VIOLATION_DETAIL);
        v.setSeverity(TEST_VIOLATION_SEVERITY);
        v.setSubgroup(TEST_VIOLATION_SUBGROUP);

        return v;
    }
}
