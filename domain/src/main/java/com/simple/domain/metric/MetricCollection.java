package com.simple.domain.metric;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.simple.original.api.analytics.IMetricCollection;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Access(AccessType.FIELD)
@Table(name = "metric_metriccollection")
public class MetricCollection extends Metric implements IMetricCollection {

	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = -1634135581163311849L;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Metric> metrics = new ArrayList<Metric>();

	public MetricCollection() {
	}

	public MetricCollection(String name) {
		super(name);
	}

	public MetricCollection(String name, List<Metric> metrics) {
		this(name);
		this.metrics = metrics;
	}

	public MetricCollection(String name, Collection<Metric> metrics) {
		this(name);
		this.metrics = new ArrayList<Metric>(metrics);
	}

	public void add(Metric metric) {
		this.metrics.add(metric);
	}

	/**
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public MetricCollection getRange(int start, int end) {
		if (end > metrics.size()) {
			end = metrics.size();
		}

		return new MetricCollection(getName(), metrics.subList(start, end));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.simple.original.taskengine.shared.metric.IMetricCollection#setValue
	 * (java.util .List)
	 */
	public void setValue(List<Metric> metrics) {
		this.metrics = metrics;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.simple.original.taskengine.shared.metric.IMetricCollection#getValue()
	 */
	@Override
	public List<Metric> getValue() {
		return metrics;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.simple.original.taskengine.shared.metric.IMetricCollection#setValue
	 * (java.util .Collection)
	 */
	public void setValue(Collection<Metric> metrics) {
		this.metrics = new ArrayList<Metric>(metrics);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.simple.orginal.api.analytics.taskengine.shared.metric.IMetricCollection#asMap()
	 */
	public Map<String, Metric> asMap() {
		Map<String, Metric> map = new HashMap<String, Metric>();
		for (Metric metric : getValue()) {
			map.put(metric.getName(), metric);
		}
		return map;
	}

	@Override
	public String getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValueAsString() {
		return "Composite Type";
	}
}
