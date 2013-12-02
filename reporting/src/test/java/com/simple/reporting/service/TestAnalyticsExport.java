package com.simple.reporting.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.simple.domain.AnalyticsOperationOutput;
import com.simple.domain.IOCDomainModule;
import com.simple.domain.dao.MetricDao;
import com.simple.domain.metric.MetricMatrix;
import com.simple.domain.metric.MetricMatrix.DoubleColumn;
import com.simple.domain.metric.MetricMatrix.StringColumn;
import com.simple.original.api.analytics.IAnalyticsOperationOutput;
import com.simple.original.api.exceptions.DomainException;
import com.simple.reporting.ExportService;

public class TestAnalyticsExport {

	
	@Inject
	private MetricDao metricDao;
	
	@Inject
	private EntityManager em;
	
    public TestAnalyticsExport() {
    	Guice.createInjector(new IOCDomainModule()).injectMembers(this);
    }
	
    @Test
	public void testExportMatrixToXls() throws IOException, DomainException {
    	AnalyticsOperationOutput dummy = new AnalyticsOperationOutput("matrix", IAnalyticsOperationOutput.Type.TWO_DIMENSIONAL);
    	
    	em.persist(dummy);
    	
		MetricMatrix matrix = new MetricMatrix();
		matrix.setOrigin(dummy);
		
		int column = 0;
		for (; column < 3; column++) {
			
			DoubleColumn dc = new DoubleColumn("Double Column " + column, randomDouble(10));
			matrix.addColumn(dc);
		}
		
		for (; column < 6; column++) {
			String[] rows = {"Row 1", "Row 2", "Row 3", "Row 4", "Row 5", "Row 6", "Row 7", "Row 8", "Row 9", "Row 10"};
			StringColumn sc = new StringColumn("String Column " + column, rows);
			matrix.addColumn(sc);
		}

		metricDao.save(matrix);
		
		assert(matrix.getId() != null);
		
		ExportService export = new ExportService();
		
		byte[] data = export.exportMetricMatrixToXls(matrix);
		
		
		assert(data != null);
		
		assert(data.length > 200);
		

		FileOutputStream fos = new FileOutputStream(new File("/tmp/testExportMatrixToXls.xls"));
		fos.write(data);
		fos.close();
		
		
    	
    	
	}
	
    double[] randomDouble(int count) {
    	double[] random = new double[count];
    	
    	//Generate random numbers of size N
    	for (int i = 0; i < count; i++) {
    	    random[i] = Math.random();
    	}
    	return random;
    }
    
	
}
