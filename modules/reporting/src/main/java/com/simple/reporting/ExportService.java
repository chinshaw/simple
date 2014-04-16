package com.simple.reporting;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ExportService {

	@Inject
	@Named("com.simple.tables.dir")
	private static String tablesDir;
	
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
