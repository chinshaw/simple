package com.simple.original.shared;

public interface AnalyticsExport {

	public static final String EXPORT_TYPE = "exportType";
	public static final String EXPORT_ID = "exportId";
	
	public enum ExportType {
		EXCEL,
		PPT,
		PDF
	}
	
	
	
}
