package com.simple.api.orchestrator;

import java.util.List;

public interface IMetricMatrix extends IMetric {

    public static interface IColumn<T extends IMetric> {
        public String getHeader();

        public void setHeader(String header);

        public List<T> getValue();

        public List<T> getCells();
    }

    public static interface IIntegerColumn extends IColumn<IMetricInteger> {
    }

    public static interface IDoubleColumn extends IColumn<IMetricDouble> {
    }

    public static interface IStringColumn extends IColumn<IMetricString> {
    }

    public abstract List<? extends IColumn<?>> getValue();

	public abstract List<String> getHeaders();

}