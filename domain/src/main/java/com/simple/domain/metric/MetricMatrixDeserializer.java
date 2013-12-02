package com.simple.domain.metric;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * This class will convert a MetricMatrix class to json
 * @author chinshaw
 *
 */
public class MetricMatrixDeserializer {
	
	private List<String> headers = new ArrayList<String>();
	private List<Column> coulumns = new ArrayList<Column>();
	
	@JsonGetter(value = "headers")
    public List<String> getHeaders() {
    	return this.headers;
    }
    
	@JsonSetter("headers")
	public void setHeaders(List<String> headers) {
    	this.headers = headers;
    }	
	
	@JsonGetter(value = "columns")
    public List<Column> getColumns() {
    	return this.coulumns;
    }
    
	@JsonSetter("columns")
	public void setColumns(List<Column> columns) {
    	this.coulumns = columns;
    }
	
	@JsonIgnoreProperties(value = {"valueAsString"})
	public static class Column {
		
		private String header;
		private List<Cell> cells = new ArrayList<Cell>();
		
		@JsonGetter(value = "header")
		public String getHeader() {
			return this.header;
		}
		
		@JsonSetter("header")
		public void setHeader(String header) {
			this.header = header;
		}
		
		@JsonGetter(value = "cells")
	    public List<Cell> getCells() {
	    	return this.cells;
	    }
	    
		@JsonSetter("cells")
		public void setCells(List<Cell> cells) {
	    	this.cells = cells;
	    }
	}
	
	@JsonIgnoreProperties(value = {"id", "version", "name", "context", "violations", "ranges", "valueAsString", "origin"})
	public static class Cell {
		
		String value;

		@JsonGetter(value = "value")
		public String getValue() {
			return this.value;
		}
		
		@JsonSetter("value")
		public void setValue(String value) {
			this.value = value;
		}
	}

}
