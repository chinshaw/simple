package com.simple.reporting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.simple.domain.export.MetricMatrixDeserializerMixin;
import com.simple.domain.model.metric.Metric;
import com.simple.domain.model.metric.MetricMatrix;
import com.simple.domain.model.metric.MetricMatrix.Column;
import com.simple.domain.model.metric.MetricMatrixDeserializer;

public class ExportService {

	@Inject
	@Named("com.simple.tables.dir")
	private static String tablesDir;
	
	/**
	 * Right now this function does not have enough error checking it is expecting
	 * a MetricCollection structured like a table which means it is a MetriColleciton of
	 * MetricCollections and each of the child metric collections will be treated as a
	 * column. It expects each column to have a MetricNumber of MetricString and should not 
	 * contain a metric collection.
	 * @param metricCollection The collection to convert to excel file. This needs to be
	 * formatted as a dataframe.
	 * @throws IOException If there is a problem writing to the ByteArrayOutputStream
	 */
	public byte[] exportMetricMatrixToXls(MetricMatrix metricMatrix) throws IOException {
		if(metricMatrix == null) {
			throw new IllegalArgumentException("matrix id must be valid long");
		}
		
		byte[] xlsData = null;

		
		String filepath = tablesDir + metricMatrix.getJsonUrl();

		ObjectMapper jsonMapper = new ObjectMapper();
		jsonMapper.addMixInAnnotations(MetricMatrixDeserializer.class, MetricMatrixDeserializerMixin.class);
		
		MetricMatrixDeserializer metricMatrixDeserializer = jsonMapper.readValue(new File(filepath), MetricMatrixDeserializer.class);

		Workbook wb = new SXSSFWorkbook(100); // keep 100 rows in memory,
												// exceeding rows will be
												// flushed to disk
		
		ByteArrayOutputStream out = null;
		
		try {
			out = new ByteArrayOutputStream();
			Sheet sheet = wb.createSheet();
		//	addMetricCollectionToSheet(sheet, metricMatrixDeserializer);
			wb.write(out);
			
			xlsData = out.toByteArray();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		
		return xlsData;
	}
	
	
	public ByteArrayOutputStream exportAnalyticsTaskExecutionToExcel(List<Metric> metrics) throws IOException {
        Workbook wb = new SXSSFWorkbook(100); // keep 100 rows in memory,
                                                // exceeding rows will be
                                                // flushed to disk
        /*
        for (Metric metric : metrics) {
            
        }
        
        Sheet sh = wb.createSheet();
         */
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        wb.write(out);
        return out;
    }
	
	
	/*private void addMetricCollectionToSheet(Sheet sheet, MetricCollection metricCollection) {
	    int columnCount = metricCollection.getValue().size();
        int rownum = 0;
        
        // Add the headers and freeze the header row.
        Row row = sheet.createRow(rownum);
        for (int headerCount = 0; headerCount < columnCount; headerCount++) {
            Cell cell = row.createCell(headerCount);
            Metric metric = metricCollection.getValue().get(headerCount);
            cell.setCellValue(metric.getName());
        }
        // Freeze the top header row.
        sheet.createFreezePane(0,1);
        
        int metricCount = 0;
        done:
        for (rownum = 1, metricCount = 0; ; rownum++, metricCount++) {
            row = sheet.createRow(rownum);
            
            int columnNum = 0;
            for (; columnNum < columnCount; columnNum++) {
                Cell cell = row.createCell(columnNum);
                
                MetricCollection column = (MetricCollection) metricCollection.getValue().get(columnNum);
                if (metricCount  < column.getValue().size()) {
                    Metric metric = column.getValue().get(metricCount);
                    if (metric instanceof MetricNumber) {
                        cell.setCellValue(((MetricNumber)metric).getValue().doubleValue()); 
                    } else if (metric instanceof MetricString) {
                        cell.setCellValue(((MetricString)metric).getValue());
                    }
                } else {
                    break done;
                }
            }
        }
	}*/
	
	/*
	private void addMetricCollectionToSheet(Sheet sheet, MetricMatrixDeserializer metricMatrixDeserializer) {
		List<String> headers = metricMatrixDeserializer.getHeaders();
		int columnCount = metricMatrixDeserializer.getHeaders().size();
        int rownum = 0;
        
        // Add the headers and freeze the header row.
        Row row = sheet.createRow(rownum);
        for (int headerCount = 0; headerCount < columnCount; headerCount++) {
            Cell cell = row.createCell(headerCount);
            cell.setCellValue(headers.get(headerCount));
        }
        // Freeze the top header row.
        sheet.createFreezePane(0,1);
        
        List<Column> columns = metricMatrixDeserializer.getColumns();
        int metricCount = 0;
        done:
        for (rownum = 1, metricCount = 0; ; rownum++, metricCount++) {
            row = sheet.createRow(rownum);
            
            int columnNum = 0;
            for (; columnNum < columnCount; columnNum++) {
                Cell cell = row.createCell(columnNum);
                
                Column column = (Column) columns.get(columnNum);
                if (metricCount  < column.getCells().size()) {
                	cell.setCellValue(column.getCells().get(rownum-1).getValue());
                } else {
                    break done;
                }
            }
        }
	}
	*/
	
	/*
	private void addMetricCollectionToSheet(Sheet sheet, int imgIndex, MetricStaticChart staticChart) {
	    
	    Drawing drawingPatriarch = sheet.createDrawingPatriarch();
	    ClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5);
	    
	    drawingPatriarch.createPicture(anchor, imgIndex);
	}
	*/
}
