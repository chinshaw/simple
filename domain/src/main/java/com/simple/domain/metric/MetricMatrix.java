package com.simple.domain.metric;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.original.api.analytics.IMetricMatrix;

/**
 * This is the representation of a table of metrics. This represents a 2d matrix
 * of metric objects.
 * 
 * @author chinshaw
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Access(AccessType.FIELD)
@Table(name = "metric_metricmatrix")
public class MetricMatrix extends Metric implements IMetricMatrix {

    /**
     * MetricMatrix serialization id.
     */
    private static final long serialVersionUID = -5149359908338640775L;

    public static abstract class Column<T extends Metric> implements IColumn<T>, Serializable {

        /**
         * Header of the columns
         */
        private String header;

        public List<T> cells = new ArrayList<T>();

        private static final long serialVersionUID = -6560460099952072154L;

        public Column() {
        }

        public Column(String header) {
            this.header = header;
        }

        public Column(List<T> cells) {
            this.cells = cells;
        }

        public Column(String header, List<T> cells) {
            this.header = header;
            this.cells = cells;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        @JsonGetter(value = "cells")
        public List<T> getValue() {
            return cells;
        }

        @JsonIgnore
        public List<T> getCells() {
            return getValue();
        }

        public void add(T cellValue) {
            cells.add(cellValue);
        }

        public String toString() {
            String string = "Metric class is -> " + getClass().getName();

            for (T value : getCells()) {
                string += "row  " + value.toString() + "\n";
            }

            return string;
        }

        public String getValueAsString() {
            return "Complex Type";
        }

    }

    public static class IntegerColumn extends Column<MetricInteger> {

        /**
         * Serialization Id.
         */
        private static final long serialVersionUID = 6976982281651068948L;

        public IntegerColumn() {
        }

        public IntegerColumn(List<MetricInteger> metrics) {
            this.getValue().addAll(metrics);
        }

        public IntegerColumn(String header, Integer[] integers) {
            super(header);
            for (Integer integer : integers) {
                add(new MetricInteger(integer));
            }
        }

        public IntegerColumn(String header, int[] integers) {
            super(header);
            for (Integer integer : integers) {
                add(new MetricInteger(integer));
            }
        }

        public void setValue(List<MetricInteger> metrics) {
            this.cells = metrics;
        }
    }

    public static class DoubleColumn extends Column<MetricDouble> {

        /**
         * Serialization ID.
         */
        private static final long serialVersionUID = 3969447586480721051L;

        public DoubleColumn() {
            super();
        }

        public DoubleColumn(String header) {
            super(header);
        }

        public DoubleColumn(String header, List<MetricDouble> metrics) {
            super(header);
            this.getValue().addAll(metrics);
        }

        public DoubleColumn(String header, Double[] doubles) {
            super(header);
            for (Double value : doubles) {
                add(new MetricDouble(value));
            }
        }

        public DoubleColumn(String header, double[] doubles) {
            super(header);
            for (Double value : doubles) {
                add(new MetricDouble(value));
            }
        }

        public void setValue(List<MetricDouble> metrics) {
            this.cells = metrics;
        }
    }

    public static class StringColumn extends Column<MetricString> {

        /**
         * Serialization ID
         */
        private static final long serialVersionUID = -6860614326192586945L;

        public StringColumn() {
            super();
        }

        public StringColumn(String header) {
            super(header);
        }

        public StringColumn(List<MetricString> metrics) {
            super(metrics);
        }

        public StringColumn(String header, List<MetricString> metrics) {
            super(header, metrics);
        }

        public StringColumn(String header, String[] values) {
            super(header);
            for (String value : values) {
                add(new MetricString(null, value));
            }
        }

        public void setValue(List<MetricString> metrics) {
            this.cells = metrics;
        }
    }

    /**
     * Columns that will be stored in this matrix.
     */
    @JsonIgnore
    @Transient
    private List<Column<?>> columns = new ArrayList<Column<?>>();

    /**
     * Object mapper to read and write the actual table object.
     */
    private static final ObjectMapper tableMapper = new ObjectMapper();

    @JsonIgnore
    private String serializedTableFile = null;

    public MetricMatrix() {
        this(null);
    }

    public MetricMatrix(String name) {
        super(name);
        tableMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.simple.original.taskengine.shared.metric.IMetricMatrix#getValue()
     */
    @JsonGetter(value = "columns")
    public List<Column<?>> getValue() {
        return columns;
    }

    /**
     * Simple accessor that just returns the the return from getValue.
     * 
     * @return
     */
    @JsonIgnore
    public List<Column<? extends Metric>> getColumns() {
        return getValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.simple.original.taskengine.shared.metric.IMetricMatrix#addColumn(com.simple.original.
     * taskengine.shared.metric.MetricMatrix.Column)
     */
    public void addColumn(Column<?> column) {
        columns.add(column);
    }

    @JsonSetter(value = "columns")
    public void setValue(List<Column<?>> columns) {
        this.columns = columns;
    }

    @Override
    public String getContext() {
        return null;
    }

    @JsonIgnore
    @Override
    public String getValueAsString() {
        return "Complex Type";
    }

    @JsonGetter(value = "headers")
    public List<String> getHeaders() {

        List<String> headers = new ArrayList<String>();
        for (Column<?> column : columns) {
            headers.add(column.getHeader());
        }
        return headers;
    }

    /**
     * Returns the name of the table file, this is where the actual data is
     * stored.
     * 
     * @return
     */
    @JsonIgnore
    public String getJsonUrl() {
        return serializedTableFile;
    }
}
